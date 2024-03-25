package net.welights.jetbrainsplugin.cttm.handler;

import com.intellij.ui.JBColor;

import net.welights.jetbrainsplugin.cttm.constants.CoinConstants;
import net.welights.jetbrainsplugin.cttm.view.AppSettingState;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public abstract class AbstractHandler {
    protected final JTable jTable;
    private final JLabel jLabel;
    private final int[] numColumnIdx = {3};
    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    public AbstractHandler(JTable table, JLabel label) {
        this.jTable = table;
        this.jLabel = label;
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        FontMetrics metrics = jTable.getFontMetrics(jTable.getFont());
        jTable.setRowHeight(Math.max(jTable.getRowHeight(), metrics.getHeight()));
    }

    protected void updateView() {
        SwingUtilities.invokeLater(() -> {
            restoreTabSizes();
            DefaultTableModel model = new DefaultTableModel(convert2Data(), getColumnNames());
            jTable.setModel(model);
            resetTabSize();
            updateRowTextColors();
            updateTimestamp();
        });
    }

    public abstract void load(List<String> symbols, int rank, String coinList);

    public abstract Object[][] convert2Data();

    public abstract String[] getColumnNames();

    public abstract void restoreTabSizes();

    public abstract void resetTabSize();

    protected void updateRowTextColors() {
        for (int idx : numColumnIdx) {
            jTable.getColumn(jTable.getColumnName(idx)).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    double chg = 0.0;
                    try {
                        String chgRaw = value.toString();
                        if (column == idx) {
                            chgRaw = chgRaw.substring(0, chgRaw.length() - 1);
                        }
                        chg = Double.parseDouble(chgRaw);
                    } catch (NumberFormatException e) {
                        chg = 0.0;
                    }
                    setForeground(getTextColor(chg));
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            });
        }
    }

    protected void updateTimestamp() {
        jLabel.setText(String.format(CoinConstants.REFRESH_TIMESTAMP, LocalDateTime.now().format(DateTimeFormatter.ofPattern(CoinConstants.TIMESTAMP_FORMATTER))));
        jLabel.setForeground(JBColor.RED);
    }

    protected String[] handleColumnNames(String[] columnNames) {
        return columnNames.clone();
    }

    public boolean isRedRise() {
        return AppSettingState.getInstance().isRedRise();
    }

    JBColor getTextColor(Double offset) {
        if (offset == 0.0) {
            return JBColor.DARK_GRAY;
        } else if (isRedRise()) {
            if (offset > 0) {
                return JBColor.RED;
            } else {
                return JBColor.GREEN;
            }
        } else {
            if (offset > 0) {
                return JBColor.GREEN;
            } else {
                return JBColor.RED;
            }
        }
    }
}

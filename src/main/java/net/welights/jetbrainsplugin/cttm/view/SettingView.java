package net.welights.jetbrainsplugin.cttm.view;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ItemEvent;

import javax.swing.*;

public class SettingView implements Configurable {
    private JPanel cttm_setting;
    //加密货币筛选框
    private JLabel coin;
    private JTextField coin_input;

    private JLabel rank;
    private JTextField rank_input;

    private JRadioButton red_rise_green_fall;
    private JRadioButton red_fall_green_rise;
    private JPanel mkt_setting_radio;
    private JLabel mkt_setting_label;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return cttm_setting.getToolTipText();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        initSettingUI();
        red_rise_green_fall.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                red_fall_green_rise.setSelected(false);
            }
        });
        red_fall_green_rise.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                red_rise_green_fall.setSelected(false);
            }
        });
        return cttm_setting;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        AppSettingState settingState = AppSettingState.getInstance();
        settingState.setCoinList(coin_input.getText());
        settingState.setRank(Integer.parseInt(rank_input.getText()));
        settingState.setRedRise(red_rise_green_fall.isSelected());
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return cttm_setting;
    }

    @Override
    public void reset() {
        initSettingUI();
    }

    private void initSettingUI() {
        AppSettingState settings = AppSettingState.getInstance();
        coin_input.setText(settings.getCoinList());
        rank_input.setText(settings.getRank() + "");
        red_rise_green_fall.setSelected(settings.isRedRise());
        red_fall_green_rise.setSelected(!settings.isRedRise());
        red_rise_green_fall.setEnabled(true);
        red_fall_green_rise.setEnabled(true);
    }

    @Override
    public void disposeUIResources() {
        cttm_setting = null;
    }

    @Override
    public void cancel() {

    }
}

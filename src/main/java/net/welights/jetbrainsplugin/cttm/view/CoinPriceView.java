package net.welights.jetbrainsplugin.cttm.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import net.welights.jetbrainsplugin.cttm.constants.CoinConstants;
import net.welights.jetbrainsplugin.cttm.handler.AbstractCoinPriceHandler;
import net.welights.jetbrainsplugin.cttm.handler.CoinPriceHandler;
import net.welights.jetbrainsplugin.cttm.util.PluginLogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;


public class CoinPriceView implements ToolWindowFactory {
    private JPanel coin_window;
    private JScrollPane coin_scroll;
    private JTable coin_table;
    private JLabel coin_timestamp;
    private JButton coin_refresh;

    private AbstractCoinPriceHandler handler;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        PluginLogUtil.init(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content coinContent = contentFactory.createContent(coin_window, CoinConstants.PLUGIN_NAME, true);

        //add content, and button listener
        toolWindow.getContentManager().addContent(coinContent);
        coin_refresh.addActionListener(e -> handler.load(parse(), AppSettingState.getInstance().getRank()));
    }

    public List<String> parse() {
        List<String> symbols = new ArrayList<>();
        String raw = AppSettingState.getInstance().getCoinList();
        assert raw != null;
        if (!raw.isEmpty()) {
            Arrays.stream(raw.split("[,; ]")).filter(s -> !s.isEmpty()).forEach(symbols::add);
        }
        return symbols;
    }

    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        handler = new CoinPriceHandler(coin_table, coin_timestamp);
        handler.load(parse(), AppSettingState.getInstance().getRank());
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public boolean isApplicable(@NotNull Project project) {
        return true;
    }
}

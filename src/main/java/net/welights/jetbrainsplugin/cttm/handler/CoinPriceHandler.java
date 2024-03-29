package net.welights.jetbrainsplugin.cttm.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.welights.jetbrainsplugin.cttm.constants.CoinConstants;
import net.welights.jetbrainsplugin.cttm.dto.CryptoCurrency;
import net.welights.jetbrainsplugin.cttm.util.HttpClientPool;
import net.welights.jetbrainsplugin.cttm.util.PluginLogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

/**
 * @author will
 */
public class CoinPriceHandler extends AbstractCoinPriceHandler {
    public CoinPriceHandler(JTable table, JLabel label) {
        super(table, label);
    }

    @Override
    public String[] getColumnNames() {
        return handleColumnNames(coinColumnNames);
    }

    @Override
    public void load(List<String> symbols, int rank, String coinList) {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            if (symbols.isEmpty()) {
                return;
            }
            for (int i = 0; i <= 3; i++) {
                try {
                    String favList = HttpClientPool.getInstance().get(CoinConstants.COIN_INFO_URL + "?ids=" + coinList);
                    String topList = HttpClientPool.getInstance().get(CoinConstants.COIN_INFO_URL + "?limit=" + rank);
                    parse(symbols, favList, topList);
                    updateView();
                    break;
                } catch (Exception e) {
                    PluginLogUtil.info(e.getMessage());
                    PluginLogUtil.info("stops updating " + jTable.getToolTipText() + " data because of " + e.getMessage());
                }
            }

        }, 0L, 2, TimeUnit.SECONDS);
        PluginLogUtil.info("updating " + jTable.getToolTipText() + " data");
    }

    //todo: 根据币种筛选
    private void parse(List<String> symbols, String favList, String topList) {
        List<CryptoCurrency> resList = new ArrayList<>();
        new JsonParser().parse(favList).getAsJsonObject().getAsJsonArray("data").forEach(element -> {
            CryptoCurrency coinInfo = new CryptoCurrency();
            JsonObject coinObj = element.getAsJsonObject();
            coinInfo.setSymbol(coinObj.get("symbol").getAsString());
            coinInfo.setName(coinObj.get("name").getAsString());
            coinInfo.setLatestPriceUs(coinObj.get("priceUsd").getAsDouble());
            coinInfo.setLatestPriceCny(0.01);
//            coinInfo.setChangeRatio1Hour(coinObj.get("percent_change_1h").getAsDouble());
            coinInfo.setChangeRatio24Hour(coinObj.get("changePercent24Hr").getAsDouble());
//            coinInfo.setChangeRatio7Day(coinObj.get("percent_change_7d").getAsDouble());
            resList.add(coinInfo);

        });

        new JsonParser().parse(topList).getAsJsonObject().getAsJsonArray("data").forEach(element -> {
            CryptoCurrency coinInfo = new CryptoCurrency();
            JsonObject coinObj = element.getAsJsonObject();
            coinInfo.setSymbol(coinObj.get("symbol").getAsString());
            coinInfo.setName(coinObj.get("name").getAsString());
            coinInfo.setLatestPriceUs(coinObj.get("priceUsd").getAsDouble());
            coinInfo.setLatestPriceCny(0.01);
//            coinInfo.setChangeRatio1Hour(coinObj.get("percent_change_1h").getAsDouble());
            coinInfo.setChangeRatio24Hour(coinObj.get("changePercent24Hr").getAsDouble());
//            coinInfo.setChangeRatio7Day(coinObj.get("percent_change_7d").getAsDouble());
            resList.add(coinInfo);
        });

        for (CryptoCurrency coin : resList ){
            updateCoinInfo(coin);
        }
    }
}

package net.welights.jetbrainsplugin.cttm.handler;


import net.welights.jetbrainsplugin.cttm.constants.CoinConstants;
import net.welights.jetbrainsplugin.cttm.dto.CryptoCurrency;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * @author will
 */
public abstract class AbstractCoinPriceHandler extends AbstractHandler {

    protected final List<CryptoCurrency> coins = new ArrayList<>();

    protected final int[] coinTabWidths = {0, 0, 0, 0, 0, 0, 0,};
    protected final String[] coinColumnNames = {
            CoinConstants.COIN_NAME,
            CoinConstants.SYMBOL,
            CoinConstants.COIN_LATEST_PRICE_CNY,
            CoinConstants.COIN_LATEST_PRICE_USD,
            CoinConstants.RISE_AND_FALL_RATIO_1Hour,
            CoinConstants.RISE_AND_FALL_RATIO_24Hour,
            CoinConstants.RISE_AND_FALL_RATIO_7Day
    };

    AbstractCoinPriceHandler(JTable table, JLabel label) {
        super(table, label);
    }

    @Override
    public void restoreTabSizes() {
        if (jTable.getColumnModel().getColumnCount() == 0) {
            return;
        }
        for (int i = 0; i < coinColumnNames.length; i++) {
            coinTabWidths[i] = jTable.getColumnModel().getColumn(i).getWidth();
        }
    }

    @Override
    public void resetTabSize() {
        for (int i = 0; i < coinColumnNames.length; i++) {
            if (coinTabWidths[i] > 0) {
                jTable.getColumnModel().getColumn(i).setWidth(coinTabWidths[i]);
                jTable.getColumnModel().getColumn(i).setPreferredWidth(coinTabWidths[i]);
            }
        }
    }

    @Override
    public Object[][] convert2Data() {
        Object[][] data = new Object[coins.size()][coinColumnNames.length];
        for (int i = 0; i < coins.size(); i++) {
            CryptoCurrency coin = coins.get(i);
            data[i] = new Object[]{
                    coin.getName(),
                    coin.getSymbol(),
                    coin.getLatestPriceCny(),
                    coin.getLatestPriceUs(),
                    coin.getChangeRatio1HourString(),
                    coin.getChangeRatio24HourString(),
                    coin.getChangeRatio7DayString()
            };
        }
        return data;
    }

    protected void updateCoinInfo(CryptoCurrency coin) {
        int idx = coins.indexOf(coin);
        if (idx > -1 && idx < coins.size()) {
            coins.set(idx, coin);
        } else {
            coins.add(coin);
        }
    }

}

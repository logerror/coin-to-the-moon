package net.welights.jetbrainsplugin.cttm.dto;

import java.util.Objects;


public class CryptoCurrency {
    //代码
    private String symbol = "";
    //名称
    private String name = "";
    //最新价格
    private double latestPriceUs = 0.0;
    private double latestPriceCny = 0.0;

    //涨跌幅
    private double changeRatio1Hour = 0.0;
    private double changeRatio24Hour = 0.0;
    private double changeRatio7Day = 0.0;

    public String getSymbol() {
        if (symbol.contains("_")) {
            return symbol.substring(symbol.indexOf("_") + 1);
        } else {
            return symbol;
        }
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatestPriceUs() {
        return latestPriceUs;
    }

    public void setLatestPriceUs(double latestPriceUs) {
        this.latestPriceUs = latestPriceUs;
    }

    public double getLatestPriceCny() {
        return latestPriceCny;
    }

    public void setLatestPriceCny(double latestPriceCny) {
        this.latestPriceCny = latestPriceCny;
    }

    public double getChangeRatio1Hour() {
        return changeRatio1Hour;
    }

    public void setChangeRatio1Hour(double changeRatio1Hour) {
        this.changeRatio1Hour = changeRatio1Hour;
    }

    public double getChangeRatio24Hour() {
        return changeRatio24Hour;
    }

    public void setChangeRatio24Hour(double changeRatio24Hour) {
        this.changeRatio24Hour = changeRatio24Hour;
    }

    public double getChangeRatio7Day() {
        return changeRatio7Day;
    }

    public void setChangeRatio7Day(double changeRatio7Day) {
        this.changeRatio7Day = changeRatio7Day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CryptoCurrency coin = (CryptoCurrency) o;
        return symbol.equals(coin.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return "Coin{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", latestPriceUs=" + latestPriceUs +
                ", changeRatio1Hour=" + changeRatio1Hour +
                '}';
    }

    public String getChangeRatio1HourString() {
        return changeRatio1Hour + "%";
    }

    public String getChangeRatio24HourString() {
        return changeRatio24Hour + "%";
    }

    public String getChangeRatio7DayString() {
        return changeRatio7Day + "%";
    }
}

import java.util.*;

public class Stock {
    private Date date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double adjClose;
    private int volume;

    //constructor
    Stock(Date date, double open, double high, double low, double close, double adjClose, int volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjClose = adjClose;
        this.volume = volume;
    }

    //getters and setters
    Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getCloseTrunc() {
        return Math.round(close * 100.0) / 100.0;
    }

    public double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    //Overwritten toString
    @Override
    public String toString() {
        return date + " " + open + " " + high + " " + low + " " + close + " " + adjClose + " " + volume;
    }
}

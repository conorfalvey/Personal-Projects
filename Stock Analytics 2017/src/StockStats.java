import java.util.*;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class StockStats {
    private ArrayList<Stock> stocks;
    private ArrayList<Stock> lower;
    private ArrayList<Stock> upper;
    private ArrayList<Stock> outliers;

    private SimpleRegression sr;
    private int size;

    private String symbol;

    private double mean;
    private double variance;
    private double stdev;
    private double q1;
    private double q3;
    private double iqr;

    private double lowerOutBound;
    private double upperOutBound;

    StockStats(ArrayList<Stock> stocks, String symbol) {
        this.stocks = stocks;
        this.symbol = symbol;

        size = stocks.size();

        lower = new ArrayList<>();
        upper = new ArrayList<>();

        setMean();
        setVariance();
        setStdev();
        setQ1();
        setQ3();

        iqr = q3 - q1;
        lowerOutBound = q1 + iqr;
        upperOutBound = q3 + iqr;

        sr = new SimpleRegression(true);
        setLinearRegression();
    }

    String getSymbol() { return symbol; }

    private void setMean() {
        double total = 0;
        for (Stock stock : stocks) {
            total += stock.getClose();
        }
        this.mean = total / stocks.size();
    }

    double getMean() { return mean; }

    private void setVariance() {
        double x_mean2Total = 0;
        for (Stock stock : stocks) {
            x_mean2Total += (stock.getClose() - mean) * (stock.getClose() - mean);
        }
        variance = x_mean2Total / stocks.size();
    }

    double getVariance() { return variance; }

    private void setStdev() {
        stdev = Math.sqrt(variance);
    }

    double getStdev() { return stdev; }

    private void setQ1() {
        for (Stock stock : stocks) {
            if (stock.getClose() < mean) {
                lower.add(stock);
            }
        }

        Collections.sort(stocks, new Comparator<>(){
            public int compare(Stock s1, Stock s2) {
                if (s1.getClose() - s2.getClose() < 0) {
                    return -1;
                } else if (s1.getClose() - s2.getClose() > 0) {
                    return 1;
                }
                return 0;
            }
        });

        if (lower.size() % 2 == 0) {
            q1 = (lower.get(lower.size() / 2 - 1).getClose() + lower.get(lower.size() / 2).getClose()) / 2;
        } else {
            q1 = lower.get((lower.size() - 1)/2).getClose();
        }
    }

    double getQ1() { return q1; }

    private void setQ3() {
        for (Stock stock : stocks) {
            if (stock.getClose() > mean) {
                upper.add(stock);
            }
        }
        Collections.sort(upper, new Comparator<>(){
            public int compare(Stock s1, Stock s2) {
                if (s1.getClose() - s2.getClose() < 0) {
                    return -1;
                } else if (s1.getClose() - s2.getClose() > 0) {
                    return 1;
                }
                return 0;
            }
        });
        if (upper.size() % 2 == 0) {
            q3 = (upper.get(upper.size() / 2 - 1).getClose() + upper.get(upper.size() / 2).getClose()) / 2;
        } else {
            q3 = upper.get((upper.size() - 1)/2).getClose();
        }
    }

    double getQ3() { return q3; }

    double getIQR() { return iqr; }

    double getLowerBound() { return lowerOutBound; }

    double getUpperBound() { return upperOutBound; }

    public void testQ1() {
        for (Stock stock : lower) {
            System.out.println(stock.getClose());
        }
    }

    public void testQ3() {
        for (Stock stock : upper) {
            System.out.println(stock.getClose());
        }
    }

    private void setOutliers() {
        ArrayList<Stock> out = new ArrayList<>();
        out.addAll(getLowerOutliers());
        out.addAll(getUpperOutliers());
        outliers = out;
    }

    private ArrayList<Stock> getLowerOutliers() {
        ArrayList<Stock> out = new ArrayList<>();
        for (Stock stock : lower) {
            if (stock.getClose() < lowerOutBound) {
                out.add(stock);
            }
        }
        return out;
    }

    private ArrayList<Stock> getUpperOutliers() {
        ArrayList<Stock> out = new ArrayList<>();
        for (Stock stock : upper) {
            if (stock.getClose() > upperOutBound) {
                out.add(stock);
            }
        }
        return out;
    }

    public ArrayList<Stock> getOutliers() {
        return outliers;
    }

    private void setLinearRegression() {
        double[][] temp = new double[2][size];
        double startOffset = stocks.get(0).getDate().getTime()/84600000;
        int count = 0;
        for (Stock stock : stocks) {
            if (count < size) {
                temp[0][count] = stock.getDate().getTime()/84600000 - startOffset;
                temp[1][count] = stock.getClose();
            } else {
                break;
            }
            count++;
        }
        sr.addData(temp);
    }

    public double getLinearSlope() { return sr.getSlope(); }

    double getLinearIntercept() { return sr.getIntercept(); }

    double getLinearSlopeTrunc() {
        return Math.round(sr.getSlope() * 10000.0) / 10000.0;
    }

    double getLinearInterceptTrunc() {
        return Math.round(sr.getIntercept() * 10000.0) / 10000.0;
    }
}

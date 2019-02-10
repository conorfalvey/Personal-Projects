import java.io.*;
import java.util.*;

public class TestMain {
    private static ArrayList<Stock> array;

    private TestMain() {
        array = new ArrayList<Stock>();
    }

    public static void main(String[] args) {
        TestMain test = new TestMain();

        test.calculate();
    }

    private void calculate() {
        //testing input
        String symbol = "AAPL";
        String date = "";

        //actual input sequence
//        Scanner input = new Scanner(System.in);
//        System.out.println("Insert Stock Symbol: ");
//        String symbol = input.nextLine().toUpperCase();
//        System.out.println("Starting Date? ");
//        String date = input.nextLine();

        String line;
        int count = 0;

        try (BufferedReader buffer = new BufferedReader(new FileReader(symbol + ".csv"))) {
            if (date.length() < 1) {
                System.out.println("No date entered, will use all of " + symbol + ".csv");
            }

            buffer.readLine();

            while ((line = buffer.readLine()) != null) {
                //split data into array and place into Stock object
                String[] data = line.split(",");

                String[] dateString = data[0].split("-");
                int[] dateInt = new int[3];
                for (int i = 0; i < 3; i++) {
                    dateInt[i] = Integer.parseInt(dateString[i]);
                }

                Stock stock = new Stock(new Date(dateInt[0] - 1900, dateInt[1], dateInt[2], 13, 0),
                        Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]),
                        Double.parseDouble(data[4]), Double.parseDouble(data[5]), Integer.parseInt(data[6]));

                if (count == 0) {
                    count++;
                } else {
                    array.add(stock);
                }
            }

        } catch (FileNotFoundException fnf) {
            System.out.println("Exception! File Not Found");
            System.exit(0);
        } catch (IOException io) {
            System.out.println("Exception! IO Exception");
            System.exit(1);
        }

        //for (Stock stock : array) {
        //System.out.println(symbol + " closed at " + stock.getCloseTrunc() + " on " + stock.getDate());
        //}

        StockStats stats = new StockStats(array, symbol );
        System.out.println("Statistics for: " + stats.getSymbol());
        System.out.println("Q1: " + stats.getQ1());
        System.out.println("Mean: " + stats.getMean());
        System.out.println("Q3: " + stats.getQ3());
        System.out.println("Standard Deviation: " + stats.getStdev());
        System.out.println("Variance: " + stats.getVariance());
        System.out.println("IQR: " + stats.getIQR());
        System.out.println("Lower Bound: " + stats.getLowerBound());
        System.out.println("Upper Bound: " + stats.getUpperBound());

        //stats.testQ1();
        //stats.testQ3();

        //ArrayList<Stock> outliers = stats.getOutliers();
        //for (Stock stock : outliers) {
        //System.out.println(stock.getClose());
        //}

        if (stats.getLinearIntercept() < 0) {
            System.out.println(stats.getSymbol() + " Linear Regression: y' = " + stats.getLinearSlopeTrunc() + "x - " + Math.abs(stats.getLinearInterceptTrunc()));
        } else {
            System.out.println(stats.getSymbol() + " Linear Regression: y' = " + stats.getLinearSlopeTrunc() + "x + " + stats.getLinearInterceptTrunc());
        }
    }
}

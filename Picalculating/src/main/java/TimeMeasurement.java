import java.math.BigDecimal;

public class TimeMeasurement {
    public static void main(String[] args) {
        int[] precisions = {50000};

        for (int precision : precisions) {
            // 单线程测量
            long startTimeSingle = System.nanoTime();
            BigDecimal piSingle = PiCalculatorSinglethreaded.calculatePi(precision);
            long endTimeSingle = System.nanoTime();
            long timeSingle = endTimeSingle - startTimeSingle;

            // 多线程测量
            long startTimeMulti = System.nanoTime();
            BigDecimal piMulti = PiCalculatorMultithreaded.calculatePi(precision);
            long endTimeMulti = System.nanoTime();
            long timeMulti = endTimeMulti - startTimeMulti;

            System.out.println("Precision: " + precision);
            System.out.println("Single-threaded time: " + timeSingle / 1000000 + " ms");
            System.out.println("Multi-threaded time: " + timeMulti / 1000000 + " ms");
            System.out.println("-------------------------------------------");
        }
    }
}
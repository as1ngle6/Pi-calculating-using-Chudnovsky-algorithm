import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PiCalculatorMultithreaded {

    public static BigDecimal calculatePi(int precision) {
        MathContext context = new MathContext(precision + 2, RoundingMode.HALF_UP);
        BigDecimal c = calculateC(context);

        int kLimit = (precision / 14) + 1; // 估计所需的项数
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<BigDecimal>> futures = new ArrayList<>();

        int kPerThread = kLimit / threadCount;
        for (int i = 0; i < threadCount; i++) {
            final int start = i * kPerThread;
            final int end = (i == threadCount - 1) ? kLimit : (i + 1) * kPerThread;
            futures.add(executor.submit(new PiTermCalculator(start, end, context)));
        }

        executor.shutdown();
        BigDecimal sum = BigDecimal.ZERO;
        try {
            for (Future<BigDecimal> future : futures) {
                sum = sum.add(future.get(), context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c.divide(sum, context).setScale(precision, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateC(MathContext context) {
        BigDecimal sqrt10005 = new BigDecimal("10005").sqrt(context);
        return new BigDecimal("426880").multiply(sqrt10005, context);
    }

    private static BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private static class PiTermCalculator implements Callable<BigDecimal> {
        private final int start;
        private final int end;
        private final MathContext context;

        PiTermCalculator(int start, int end, MathContext context) {
            this.start = start;
            this.end = end;
            this.context = context;
        }

        @Override
        public BigDecimal call() {
            BigDecimal partialSum = BigDecimal.ZERO;
            for (int k = start; k < end; k++) {
                BigInteger factorial6k = factorial(6 * k);
                BigInteger term1 = BigInteger.valueOf(13591409 + 545140134L * k);
                BigInteger sign = (k % 2 == 0) ? BigInteger.ONE : BigInteger.ONE.negate();
                BigInteger numerator = factorial6k.multiply(term1).multiply(sign);

                BigInteger factorial3k = factorial(3 * k);
                BigInteger factorialK3 = factorial(k).pow(3);
                BigInteger denominator = factorial3k.multiply(factorialK3)
                        .multiply(BigInteger.valueOf(640320).pow(3 * k));

                BigDecimal term = new BigDecimal(numerator, context)
                        .divide(new BigDecimal(denominator, context), context);
                partialSum = partialSum.add(term, context);
            }
            return partialSum;
        }
    }

    public static void main(String[] args) {
        int precision = 100; // 计算100位圆周率
        
        BigDecimal pi = calculatePi(precision);
        System.out.println(pi);
    }
}
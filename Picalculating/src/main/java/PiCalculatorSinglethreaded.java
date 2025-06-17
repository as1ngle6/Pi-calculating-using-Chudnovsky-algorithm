import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PiCalculatorSinglethreaded {

    public static BigDecimal calculatePi(int precision) {
        MathContext context = new MathContext(precision + 2, RoundingMode.HALF_UP);
        BigDecimal c = calculateC(context);

        int kLimit = (precision / 14) + 1; // 估计所需的项数
        BigDecimal sum = calculateSum(kLimit, context);

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

    private static BigDecimal calculateSum(int kLimit, MathContext context) {
        BigDecimal partialSum = BigDecimal.ZERO;
        for (int k = 0; k < kLimit; k++) {
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

    public static void main(String[] args) {
        int precision = 100000; // 计算100位圆周率
        BigDecimal pi = calculatePi(precision);
        System.out.println(pi);
    }
}
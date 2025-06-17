import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryMeasurement {
    // 内存采样器内部类
    private static class MemorySampler implements Runnable {
        private final AtomicLong peakMemory = new AtomicLong(0);
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                long usedMem = getUsedMemory();
                // 原子更新峰值内存
                long currentPeak = peakMemory.get();
                if (usedMem > currentPeak) {
                    peakMemory.compareAndSet(currentPeak, usedMem);
                }
                try {
                    Thread.sleep(5); // 高频采样（5ms）
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        public void stop() {
            running = false;
        }

        public long getPeakMemory() {
            return peakMemory.get();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] precisions = {10000};
        
        for (int precision : precisions) {
            // 单线程测试
            stabilizeMemory();
            long baseMemory = getUsedMemory();
            MemorySampler samplerSingle = new MemorySampler();
            Thread samplerThreadSingle = new Thread(samplerSingle);
            samplerThreadSingle.setDaemon(true);
            samplerThreadSingle.start();
            
            BigDecimal pSingle = PiCalculatorSinglethreaded.calculatePi(precision);
            
            samplerSingle.stop();
            samplerThreadSingle.interrupt();
            long peakMemorySingle = samplerSingle.getPeakMemory();
            long memoryUsageSingle = peakMemorySingle - baseMemory;
            
            // 多线程测试
            stabilizeMemory();
            baseMemory = getUsedMemory();
            MemorySampler samplerMulti = new MemorySampler();
            Thread samplerThreadMulti = new Thread(samplerMulti);
            samplerThreadMulti.setDaemon(true);
            samplerThreadMulti.start();
            
            BigDecimal pMulti = PiCalculatorMultithreaded.calculatePi(precision);
            
            samplerMulti.stop();
            samplerThreadMulti.interrupt();
            long peakMemoryMulti = samplerMulti.getPeakMemory();
            long memoryUsageMulti = peakMemoryMulti - baseMemory;
            
            System.out.println("Precision: " + precision);
            System.out.println("Single-threaded memory usage: " + memoryUsageSingle + " bytes");
            System.out.println("Multi-threaded memory usage: " + memoryUsageMulti + " bytes");
            System.out.println("----------------------------------");
        }
    }
    
    // 稳定内存状态
    private static void stabilizeMemory() throws InterruptedException {
        long current, previous;
        int stableCount = 0;
        do {
            System.gc();
            Thread.sleep(150);
            previous = getUsedMemory();
            
            System.gc();
            Thread.sleep(150);
            current = getUsedMemory();
            
            // 连续3次内存变化小于1KB视为稳定
            if (Math.abs(current - previous) < 1024) {
                stableCount++;
            } else {
                stableCount = 0;
            }
        } while (stableCount < 3);
    }

    // 获取当前内存使用量
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
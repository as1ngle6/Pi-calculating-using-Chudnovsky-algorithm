import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PiPerformanceVisualizer {
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 16);
    
    static {
        System.setProperty("org.jfree.chart.noppletopics", "true");
        System.setProperty("java.awt.headless", "false");
    }

    public static void main(String[] args) {
        // 计算精度
        int[] precisions = {100, 1000, 10000, 50000, 100000};
        
        // 转换后的时间数据（毫秒 -> 秒）
        double[] singleThreadTimes = {0.030, 0.058, 4.008, 385.697, 2800.941};
        double[] multiThreadTimes = {0.011, 0.047, 1.328, 113.658, 854.352};
        
        // 转换后的内存数据（字节 -> MB）
        double[] singleThreadMemory = {1740512.0/(1024*1024), 
                                      2233384.0/(1024*1024), 
                                      198387408.0/(1024*1024), 
                                      258736480.0/(1024*1024), 
                                      427974008.0/(1024*1024)};
        
        double[] multiThreadMemory = {85544.0/(1024*1024), 
        							1620288.0/(1024*1024), 
        							590870304.0/(1024*1024), 
        							685268400.0/(1024*1024), 
        							1095973592.0/(1024*1024)};

        // 创建时间数据集
        XYSeriesCollection timeDataset = createTimeDataset(precisions, singleThreadTimes, multiThreadTimes);
        
        // 创建内存数据集
        XYSeriesCollection memoryDataset = createMemoryDataset(precisions, singleThreadMemory, multiThreadMemory);

        // 创建时间图表
        JFreeChart timeChart = createTimeChart(timeDataset);
        
        // 创建内存图表
        JFreeChart memoryChart = createMemoryChart(memoryDataset);

        // 显示图表
        displayChart(timeChart, "Chudnovsky Algorithm Time Performance");
        displayChart(memoryChart, "Chudnovsky Algorithm Memory Usage");
    }

    private static XYSeriesCollection createTimeDataset(int[] precisions, 
                                                        double[] singleThreadTimes,
                                                        double[] multiThreadTimes) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        // 添加单线程实际测量点
        XYSeries singleSeries = new XYSeries("Single-thread Actual");
        for (int i = 0; i < precisions.length; i++) {
            singleSeries.add(precisions[i], singleThreadTimes[i]);
        }
        dataset.addSeries(singleSeries);
        
        // 添加多线程实际测量点
        XYSeries multiSeries = new XYSeries("Multi-thread Actual");
        for (int i = 0; i < precisions.length; i++) {
            multiSeries.add(precisions[i], multiThreadTimes[i]);
        }
        dataset.addSeries(multiSeries);
        
        return dataset;
    }
    
    private static XYSeriesCollection createMemoryDataset(int[] precisions, 
                                                         double[] singleThreadMemory,
                                                         double[] multiThreadMemory) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        // 添加单线程实际内存使用点
        XYSeries singleSeries = new XYSeries("Single-thread Actual");
        for (int i = 0; i < precisions.length; i++) {
            singleSeries.add(precisions[i], singleThreadMemory[i]);
        }
        dataset.addSeries(singleSeries);
        
        // 添加多线程实际内存使用点
        XYSeries multiSeries = new XYSeries("Multi-thread Actual");
        for (int i = 0; i < precisions.length; i++) {
            multiSeries.add(precisions[i], multiThreadMemory[i]);
        }
        dataset.addSeries(multiSeries);
        
        return dataset;
    }

    private static JFreeChart createTimeChart(XYSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Chudnovsky Algorithm Time Performance",
                "Calculation Precision (Digits)",
                "Execution Time (Seconds)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        
        customizeTimeChart(chart);
        return chart;
    }
    
    private static JFreeChart createMemoryChart(XYSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Chudnovsky Algorithm Memory Usage",
                "Calculation Precision (Digits)",
                "Memory Usage (MB)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        
        customizeMemoryChart(chart);
        return chart;
    }

    private static void customizeTimeChart(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        
        // 设置字体
        chart.getTitle().setFont(TITLE_FONT);
        plot.getDomainAxis().setLabelFont(DEFAULT_FONT);
        plot.getRangeAxis().setLabelFont(DEFAULT_FONT);
        chart.getLegend().setItemFont(DEFAULT_FONT);
        
        // 设置对数轴
        LogAxis domainAxis = new LogAxis("Calculation Precision (Digits)");
        domainAxis.setBase(10);
        domainAxis.setLabelFont(DEFAULT_FONT);
        plot.setDomainAxis(domainAxis);
        
        LogAxis rangeAxis = new LogAxis("Execution Time (Seconds)");
        rangeAxis.setBase(10);
        rangeAxis.setLabelFont(DEFAULT_FONT);
        plot.setRangeAxis(rangeAxis);
        
        // 配置渲染器
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        // 单线程实际数据（红色方块+实线）
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesShape(0, new Rectangle(6, 6));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(0, true);
        
        // 多线程实际数据（蓝色圆圈+实线）
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesShape(1, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(1, true);
        
        plot.setRenderer(renderer);
        
        // 添加标注
        addAnnotation(plot, "Max Speedup: 3.39x at 50,000 digits", 50000, 100);
        addAnnotation(plot, "100,000 digits: Single=46.7min, Multi=14.2min", 100000, 10);
    }
    
    private static void customizeMemoryChart(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        
        // 设置字体
        chart.getTitle().setFont(TITLE_FONT);
        plot.getDomainAxis().setLabelFont(DEFAULT_FONT);
        plot.getRangeAxis().setLabelFont(DEFAULT_FONT);
        chart.getLegend().setItemFont(DEFAULT_FONT);
        
        // 设置对数轴
        LogAxis domainAxis = new LogAxis("Calculation Precision (Digits)");
        domainAxis.setBase(10);
        domainAxis.setLabelFont(DEFAULT_FONT);
        plot.setDomainAxis(domainAxis);
        
        LogAxis rangeAxis = new LogAxis("Memory Usage (MB)");
        rangeAxis.setBase(10);
        rangeAxis.setLabelFont(DEFAULT_FONT);
        plot.setRangeAxis(rangeAxis);
        
        // 配置渲染器
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        // 单线程实际内存（红色三角形+实线）
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesShape(0, new Polygon(
            new int[]{0, 6, 3}, 
            new int[]{0, 0, 6}, 
            3
        ));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(0, true);
        
        // 多线程实际内存（蓝色三角形+实线）
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesShape(1, new Polygon(
            new int[]{0, 6, 3}, 
            new int[]{0, 0, 6}, 
            3
        ));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(1, true);
        
        plot.setRenderer(renderer);
        
        // 添加标注
        addAnnotation(plot, "10,000 digits: Multi uses 47x more memory", 10000, 100);
        addAnnotation(plot, "100,000 digits: Multi uses 1.05GB", 100000, 1000);
    }

    private static void addAnnotation(XYPlot plot, String text, double x, double y) {
        XYTextAnnotation annotation = new XYTextAnnotation(text, x, y);
        annotation.setFont(new Font("SansSerif", Font.BOLD, 12));
        annotation.setPaint(Color.DARK_GRAY);
        plot.addAnnotation(annotation);
    }

    private static void displayChart(JFreeChart chart, String title) {
        ChartFrame frame = new ChartFrame(title, chart);
        frame.setSize(900, 700);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
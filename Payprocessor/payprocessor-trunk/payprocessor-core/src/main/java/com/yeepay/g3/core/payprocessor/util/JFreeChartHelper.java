package com.yeepay.g3.core.payprocessor.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/10/12.
 */
public class JFreeChartHelper {

    /**
     * 设置文件主题
     */
    private static void setChartTheme() {
        //创建主题样式
        StandardChartTheme theme = new StandardChartTheme("CN");
        //设置标题字体
        theme.setExtraLargeFont(new Font("隶书", Font.BOLD, 18));
        //设置图例的字体
        theme.setRegularFont(new Font("宋书", Font.PLAIN, 10));
        //设置轴向的字体
        theme.setLargeFont(new Font("宋书", Font.PLAIN, 12));
        theme.setPlotBackgroundPaint(Color.WHITE);
        ChartFactory.setChartTheme(theme);
    }

    /**
     * @param chartData@return
     */
    private static JFreeChart generateBarChart(ChartData chartData) {
        DefaultCategoryDataset dataSet = generateDataSet(chartData);
        JFreeChart chart = ChartFactory.createBarChart(chartData.getTitle(), // chart title
                chartData.getCategoryAxisLabel(), // domain axis label
                chartData.getValueAxisLabel(), // range axis label
                dataSet, // data
                PlotOrientation.VERTICAL, // 图标方向
                true, // 是否显示legend
                true, // 是否显示tooltips
                false // 是否显示URLs
        );
        return chart;
    }

    /**
     * 生成时间序列数组
     *
     * @param chartData
     * @return
     */
    private static JFreeChart generateTimeSeriesChart(ChartData chartData) {
        JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart(
                chartData.getTitle(),
                chartData.getCategoryAxisLabel(),
                chartData.getValueAxisLabel(),
                generateXYDataSet(chartData),//createDataSet()
                true,
                true, false);
        XYPlot plot = timeSeriesChart.getXYPlot();
        setXYPolt(plot);
        return timeSeriesChart;
    }

    /**
     * 生成图表数据
     *
     * @param chartData
     * @return
     */
    private static DefaultCategoryDataset generateDataSet(ChartData chartData) {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (String key : chartData.getDataMap().keySet()) {
            Object value = chartData.getDataMap().get(key);
            if (value == null)
                continue;
            if (value instanceof Map) {
                Map<String, Object> map = (Map) value;
                for (String key1 : map.keySet()) {
                    dataSet.addValue((Number) map.get(key1), key, key1);
                }
            }
        }
        return dataSet;
    }

    /**
     * 生成时间序列数据
     *
     * @param chartData
     * @return
     */
    private static XYDataset generateXYDataSet(ChartData chartData) {
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        for (String key : chartData.getDataMap().keySet()) {
            TimeSeries timeSeries1 = new TimeSeries(key);
            Object value = chartData.getDataMap().get(key);
            if (value == null)
                continue;
            if (value instanceof Map) {
                Map<String, Object> map = (Map) value;
                for (String $key : map.keySet()) {
                    String[] timeArray = $key.split("-");
                    if (timeArray == null)
                        continue;
                    if (timeArray.length == 3) {
                        Day day = new Day(Integer.valueOf(timeArray[2]), Integer.valueOf(timeArray[1]), Integer.valueOf(timeArray[0]));
                        timeSeries1.add(day, (Number) map.get($key));
                    }
                    if (timeArray.length == 2) {
                        Month month = new Month(Integer.valueOf(timeArray[1]), Integer.valueOf(timeArray[0]));
                        timeSeries1.add(month, (Number) map.get($key));
                    }
                }
            }
            dataSet.addSeries(timeSeries1);
        }
        return dataSet;
    }

    /**
     * 创建BarChart
     *
     * @return 生成Chart图片的地址
     * @author 马建新（mervin）
     */
    public static byte[] createBarChart(ChartData chartData) throws IOException {
        setChartTheme();
        JFreeChart chart = generateBarChart(chartData);
        return toByteArray(chart);
    }

    /**
     * 生成时间序列图表字节数组
     *
     * @param chartData
     * @return
     * @throws IOException
     */
    public static byte[] createTimeSeriesChart(ChartData chartData) throws IOException {
        setChartTheme();
        JFreeChart chart = generateTimeSeriesChart(chartData);
        return toByteArray(chart);
    }

    /**
     * 转换为字节数组
     *
     * @param chart
     * @return
     * @throws IOException
     */
    private static byte[] toByteArray(JFreeChart chart) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(byteArrayOutputStream, chart, 500, 400);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 设置点格式
     *
     * @param plot
     */
    private static void setXYPolt(XYPlot plot) {
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        // plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(false);
        }
    }
}

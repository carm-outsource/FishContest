import cc.carm.outsource.plugin.fishcontest.conf.quantity.impl.GaussianQuantity;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GaussianQuantityTest {
    protected final GaussianQuantity quantity = new GaussianQuantity(100, 2000, 5000);

    @Test
    public void singleTest() {
        System.out.println(quantity.randomValue());
    }


    @Test
    public void test() throws IOException {


        Map<Integer, Integer> values = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            int v = quantity.randomValue();

            if (values.containsKey(v)) {
                values.put(v, values.get(v) + 1);
            } else {
                values.put(v, 1);
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("values");
        values.forEach(series1::add);
        dataset.addSeries(series1);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Testing Gaussian Quantity", "", "", dataset
        );


        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 228, 196));

        ChartUtilities.saveChartAsPNG(new File("target/gaussian-test.png"), chart, 2000, 1000);

    }


}

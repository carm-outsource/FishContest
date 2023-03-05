import org.junit.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MeasurementTest {


    @Test
    public void test() {

        System.out.println(getQuantityString(5657));
        System.out.println(getQuantityString(55625));

    }

    private static String getQuantityString(int quantity) {
        // The measurement map from PluginConfig is an order-reversed TreeMap
        Map<Integer, String> measurement = new TreeMap<>(Comparator.reverseOrder());
        measurement.put(1000, "A ");
        measurement.put(500, "B ");
        measurement.put(10, "C ");
        measurement.put(1, "D ");

        int cache = quantity;
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<Integer, String> entry : measurement.entrySet()) {
            int unit = entry.getKey();
            String name = entry.getValue();

            if (cache > unit) {
                int count = cache / unit;
                cache = cache % unit;
                builder.append(count).append(name);
            }
        }

        return builder.toString();
    }

}

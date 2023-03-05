package cc.carm.outsource.plugin.fishcontest.conf.quantity;

import cc.carm.outsource.plugin.fishcontest.Main;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.impl.FixedQuantity;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.impl.GaussianQuantity;
import cc.carm.outsource.plugin.fishcontest.conf.quantity.impl.RangeQuantity;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public interface QuantityValue {

    int randomValue();

    String serialize();

    static @Nullable QuantityValue parse(String conf) {
        try {
            int[] values = Arrays.stream(conf.split(";")).mapToInt(Integer::parseInt).toArray();
            return switch (values.length) {
                case 1 -> new FixedQuantity(values[0]);
                case 2 -> new RangeQuantity(values[0], values[1]);
                case 3 -> new GaussianQuantity(values[0], values[1], values[2]);
                default -> throw new UnsupportedOperationException("Unsupported quantity value: " + conf);
            };
        } catch (Exception ex) {
            Main.severe("无法解析数量配置值，将使用默认值: " + ex.getMessage() + "");
            return null;
        }
    }

    static QuantityValue defaults() {
        return new RangeQuantity(200, 500);
    }

}

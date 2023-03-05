package cc.carm.outsource.plugin.fishcontest.conf.quantity.impl;

import cc.carm.outsource.plugin.fishcontest.conf.quantity.QuantityValue;

public record RangeQuantity(int min, int max) implements QuantityValue {

    @Override
    public int randomValue() {
        return (int) (Math.random() * (max() - min() + 1) + min());
    }

    @Override
    public String serialize() {
        return min + ";" + max;
    }

}

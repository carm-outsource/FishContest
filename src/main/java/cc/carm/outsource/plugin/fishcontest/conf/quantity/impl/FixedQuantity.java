package cc.carm.outsource.plugin.fishcontest.conf.quantity.impl;

import cc.carm.outsource.plugin.fishcontest.conf.quantity.QuantityValue;

public record FixedQuantity(int value) implements QuantityValue {

    @Override
    public int randomValue() {
        return value;
    }

    @Override
    public String serialize() {
        return String.valueOf(value);
    }

}

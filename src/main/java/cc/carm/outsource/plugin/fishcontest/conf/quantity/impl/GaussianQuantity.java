package cc.carm.outsource.plugin.fishcontest.conf.quantity.impl;

import cc.carm.outsource.plugin.fishcontest.conf.quantity.QuantityValue;

import java.util.Random;

public record GaussianQuantity(int min, int avg, int max) implements QuantityValue {

    @Override
    public int randomValue() {
        Random random = new Random();

        if (max <= min) throw new IllegalArgumentException("Argument max must be larger than min.");

        double sigma = (max - min) / 2.5D;
        double res = random.nextGaussian() * sigma + avg;
        if (res > max || res < min) return randomValue();

        return (int) res;
    }

    @Override
    public String serialize() {
        return min + ";" + avg + ";" + max;
    }

}

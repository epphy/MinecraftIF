package com.mif.mif.core.feature;

@FunctionalInterface
public interface FeatureFactory<T> {
    T create(FeatureContext featureContext);
}

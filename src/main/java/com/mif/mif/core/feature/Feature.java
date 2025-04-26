package com.mif.mif.core.feature;

public interface Feature {
    void enable();
    void disable();
    FeatureId getFeatureId();
}

package com.mif.mif.core.feature;

public interface Feature {
    void enable(FeatureContext featureContext);
    void disable();
    FeatureId getFeatureId();
}

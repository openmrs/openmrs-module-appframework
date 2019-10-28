package org.openmrs.module.appframework.feature;

/**
 *	Provides a way to instantiate {@link FeatureToggleProperties} in unit tests.
 */
public class TestFeatureTogglePropertiesFactory {
	
	public static FeatureToggleProperties get() {
		return new FeatureToggleProperties();
	}
}

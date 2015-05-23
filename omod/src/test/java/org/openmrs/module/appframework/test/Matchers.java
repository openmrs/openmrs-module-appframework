package org.openmrs.module.appframework.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.openmrs.util.OpenmrsUtil;

import java.util.Map;

public class Matchers {

    /**
     * @param key
     * @param expected a Matcher (or an Object, which will be tested with OpenmrsUtil.nullSafeEquals)
     * @return a matcher that matches a Map entry (for key) matching expected
     */
    public static Matcher<? super Map<String, ?>> hasEntry(final String key, final Object expected) {
        return new BaseMatcher<Map<String, ?>>() {
            @Override
            public boolean matches(Object o) {
                Object actual = ((Map) o).get(key);
                if (expected instanceof Matcher) {
                    return ((Matcher) expected).matches(actual);
                }
                else {
                    return OpenmrsUtil.nullSafeEquals(actual, expected);
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("map entry " + key + " should ");
                if (expected instanceof Matcher) {
                    description.appendDescriptionOf((SelfDescribing) expected);
                }
                else {
                    description.appendText("equal " + expected);
                }
            }
        };
    }

}

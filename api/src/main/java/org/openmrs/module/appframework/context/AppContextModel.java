package org.openmrs.module.appframework.context;

import java.util.HashMap;

/**
 * Simple Context Model that an app can use to provide context when requesting extensions, etc
 * from the AppFrameworkService
 */
public class AppContextModel extends HashMap<String, Object> {

    /**
     * @param key
     * @param value
     * @return a shallow copy, with key->value added
     */
    public AppContextModel with(String key, Object value) {
        AppContextModel clone = new AppContextModel();
        clone.putAll(this);
        clone.put(key, value);
        return clone;
    }

    /**
     * @param key
     * @return a shallow copy, with 'key' removed
     */
    public AppContextModel without(String key) {
        AppContextModel clone = new AppContextModel();
        clone.putAll(this);
        clone.remove(key);
        return clone;
    }

}

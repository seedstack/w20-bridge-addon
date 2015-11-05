/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a configured module in a W20 fragment.
 *
 * @author adrien.lauer@mpsa.com
 */
public class ConfiguredModule implements Serializable {
    private String name;
    private boolean enabled;
    private Map<String, Object> configuration = new HashMap<String, Object>();

    /**
     * Gets the name of the module.
     *
     * @return the name of the module.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the module.
     *
     * @param name the value to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the module status.
     *
     * @return true if module is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the module status.
     *
     * @param enabled the value to set.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the module configuration.
     *
     * @return the module configuration map.
     */
    @JsonAnyGetter
    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    /**
     * Sets the module configuration.
     *
     * @param configuration the configuration map to set.
     */
    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }

    /**
     * Put a value in the module configuration map.
     *
     * @param key the key.
     * @param value the value.
     */
    @JsonAnySetter
    public void putConfigurationValue(String key, Object value) {
        this.configuration.put(key, value);
    }
}

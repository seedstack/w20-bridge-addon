/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal.rest.application;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a configured W20 fragment.
 *
 * @author adrien.lauer@mpsa.com
 */
public class ConfiguredFragmentRepresentation {
    private boolean preload;
    private boolean optional;
    private boolean ignore;
    private Map<String, Object> modules = new HashMap<>();
    private Map<String, String> vars = new HashMap<>();

    /**
     * @return true if the fragment can be preloaded, false otherwise.
     */
    public boolean isPreload() {
        return preload;
    }

    /**
     * Sets if the fragment can be preloaded.
     *
     * @param preload true if the fragment can be preloaded, false otherwise.
     */
    public void setPreload(boolean preload) {
        this.preload = preload;
    }

    /**
     * @return true if the fragment is optional, false otherwise.
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Sets if the fragment is optional.
     * @param optional true if the fragment is optional, false otherwise.
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    /**
     * @return true if the fragment should be ignored, false otherwise.
     */
    public boolean isIgnore() {
        return ignore;
    }

    /**
     * Sets if the fragment should be ignored.
     * @param ignore true if the fragment should be ignored, false otherwise.
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    /**
     * @return the configured modules of this fragment.
     */
    public Map<String, Object> getModules() {
        return modules;
    }

    /**
     * Sets the configured modules of this fragment.
     *
     * @param modules the value to set.
     */
    public void setModules(Map<String, Object> modules) {
        this.modules = modules;
    }

    /**
     * @return the fragment variables.
     */
    public Map<String, String> getVars() {
        return vars;
    }

    /**
     * Set the fragment variables.
     *
     * @param vars the value to set.
     */
    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }
}

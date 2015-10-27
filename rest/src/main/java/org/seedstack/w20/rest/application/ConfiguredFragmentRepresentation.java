/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.rest.application;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a configured W20 fragment.
 *
 * @author adrien.lauer@mpsa.com
 */
public class ConfiguredFragmentRepresentation {
    private boolean preload;
    private Map<String, Object> modules = new HashMap<String, Object>();
    private Map<String, String> vars = new HashMap<String, String>();

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

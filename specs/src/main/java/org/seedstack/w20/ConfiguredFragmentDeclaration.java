/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;

import java.util.Map;

/**
 * This class represents a W20 fragment configuration declaration.
 *
 * @author adrien.lauer@mpsa.com
 */
public class ConfiguredFragmentDeclaration implements FragmentDeclaration {
    private Boolean optional;
    private Boolean ignore;
    private Boolean preload;
    private String name;
    private Map<String, ConfiguredModule> modules;
    private Map<String, Object> routes;
    private Map<String, String> vars;
    private boolean enabled;
    private String manifestLocation;

    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the fragment.
     *
     * @param name the fragment name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the configured modules of this configured fragment.
     *
     * @return a map of {@link ConfiguredModule} keyed by their name.
     */
    public Map<String, ConfiguredModule> getModules() {
        return modules;
    }

    /**
     * Sets the configured modules of this configured fragment.
     *
     * @param modules a map of {@link ConfiguredModule} keyed by their name.
     */
    public void setModules(Map<String, ConfiguredModule> modules) {
        this.modules = modules;
    }

    /**
     * Gets the configured routes of this configured fragment.
     *
     * @return a map of raw JSON objects.
     */
    public Map<String, Object> getRoutes() {
        return routes;
    }

    /**
     * Sets the configured routes of this configured fragment
     *
     * @param routes a map of raw JSON objects.
     */
    public void setRoutes(Map<String, Object> routes) {
        this.routes = routes;
    }

    /**
     * Gets the variables of this fragment.
     *
     * @return the map of fragment variables keyed by their name.
     */
    public Map<String, String> getVars() {
        return vars;
    }

    /**
     * Sets the variables of this fragment.
     *
     * @param vars the map of fragment variables keyed by their name.
     */
    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    /**
     * Gets the fragment status.
     *
     * @return true if fragment is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the fragment status.
     *
     * @param enabled the value to set.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the fragment preload status.
     *
     * @return true if preload is enabled, false if preload is not enabled, null if not defined.
     */
    public Boolean isPreload() {
        return preload;
    }

    /**
     * Sets the fragment preload status.
     *
     * @param preload the value to set.
     */
    public void setPreload(Boolean preload) {
        this.preload = preload;
    }

    /**
     * Gets the fragment manifest location.
     *
     * @return the fragment manifest resource path.
     */
    public String getManifestLocation() {
        return manifestLocation;
    }

    /**
     * Sets the fragment manifest location.
     *
     * @param manifestLocation the resource path to set.
     */
    public void setManifestLocation(String manifestLocation) {
        this.manifestLocation = manifestLocation;
    }

    /**
     * Gets the fragment optional status.
     *
     * @return true if the fragment is optional, false otherwise.
     */
    public Boolean isOptional() {
        return optional;
    }

    /**
     * Sets the fragment optional status.
     *
     * @param optional true to set the fragment as optional, false otherwise.
     */
    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    /**
     * Gets the fragment ignore status.
     *
     * @return true if the fragment should be ignored, false otherwise.
     */
    public Boolean isIgnore() {
        return ignore;
    }

    /**
     * Sets the fragment ignore status.
     *
     * @param ignore true to set the fragment as ignored, false otherwise.
     */
    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }
}

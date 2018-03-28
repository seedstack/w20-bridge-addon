/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.spi;

import java.util.Map;

/**
 * This interface can be implemented to override W20 default configuration with custom code.
 *
 * @author adrien.lauer@mpsa.com
 */
public interface FragmentConfigurationHandler {
    /**
     * Provides the opportunity to override if a fragment is enabled or not. This method is called for each fragment
     * detected.
     * @param fragmentName the fragment name to override.
     * @return true if the fragment should be enabled, false if the fragment should be disabled, null if it shouldn't be altered by this handler.
     */
    public Boolean overrideFragmentStatus(String fragmentName);

    /**
     * Provides the opportunity to override a module status inside a fragment. This method is called for each module
     * of each fragment detected.
     * @param fragmentName the fragment name to override.
     * @param moduleName the module name to override.
     * @return true if the module should be enabled, false if the module should be disabled, null if it shouldn't be altered by this handler.
     */
    public Boolean overrideModuleStatus(String fragmentName, String moduleName);

    /**
     * Provides the opportunity to override a module configuration. This method is called for each module of each fragment
     * detected.
     * @param fragmentName the fragment name to override.
     * @param moduleName the module name to override.
     * @param sourceConfiguration the writable module configuration.
     */
    public void overrideConfiguration(String fragmentName, String moduleName, Map<String, Object> sourceConfiguration);

    /**
     * Provides the opportunity to override a fragment variables. This method is called for each fragment detected.
     * @param fragmentName the fragment name to override.
     * @param variables the writable fragment variables.
     */
    public void overrideVariables(String fragmentName, Map<String, String> variables);
}

/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

import java.util.Map;

class GlobalConfigurationHandler implements FragmentConfigurationHandler {
    @Override
    @SuppressFBWarnings(value = "NP_BOOLEAN_RETURN_NULL", justification = "null used to denote no change")
    public Boolean overrideFragmentStatus(String fragmentName) {
        if ("w20-core".equals(fragmentName)) {
            return true;
        }

        return null;
    }

    @Override
    @SuppressFBWarnings(value = "NP_BOOLEAN_RETURN_NULL", justification = "null used to denote no change")
    public Boolean overrideModuleStatus(String fragmentName, String moduleName) {
        return null;
    }

    @Override
    public void overrideConfiguration(String fragmentName, String moduleName, Map<String, Object> sourceConfiguration) {
        // nothing to do here
    }

    @Override
    public void overrideVariables(String fragmentName, Map<String, String> variables) {
        // nothing to do here
    }
}

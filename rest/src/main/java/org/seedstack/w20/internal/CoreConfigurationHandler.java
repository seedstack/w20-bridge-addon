/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;


import org.seedstack.w20.spi.FragmentConfigurationHandler;
import org.seedstack.seed.core.api.Application;
import org.seedstack.seed.core.api.Configuration;

import javax.inject.Inject;
import java.util.Map;

class CoreConfigurationHandler implements FragmentConfigurationHandler {
    private final Application application;

    @Configuration(value = "org.seedstack.w20.environment", mandatory = false)
    private String environment;

    @Inject
    CoreConfigurationHandler(Application application) {
        this.application = application;
    }

    @Override
    public Boolean overrideFragmentStatus(String fragmentName) {
        if ("w20-core".equals(fragmentName)) {
            return true;
        }

        return null;
    }

    @Override
    public Boolean overrideModuleStatus(String fragmentName, String moduleName) {
        if ("w20-core".equals(fragmentName)) {
            if ("application".equals(moduleName)) {
                return true;
            } else if ("env".equals(moduleName)) {
                return true;
            } else if ("security".equals(moduleName)) {
                return true;
            }
        }

        return null;
    }

    @Override
    public void overrideConfiguration(String fragmentName, String moduleName, Map<String, Object> sourceConfiguration) {
        if ("w20-core".equals(fragmentName)) {
            if ("application".equals(moduleName)) {
                sourceConfiguration.put("id", application.getId());
            }

            if ("env".equals(moduleName)) {
                if (!sourceConfiguration.containsKey("type") && environment != null) {
                    sourceConfiguration.put("type", environment);
                }
            }
        }
    }

    @Override
    public void overrideVariables(String fragmentName, Map<String, String> variables) {
        // nothing to do here
    }
}

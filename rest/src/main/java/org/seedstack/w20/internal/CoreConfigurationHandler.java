/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;


import org.seedstack.seed.Application;
import org.seedstack.seed.Configuration;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

import javax.inject.Inject;
import java.util.Map;

class CoreConfigurationHandler implements FragmentConfigurationHandler {
    private final Application application;

    @Configuration(value = "org.seedstack.w20.environment", mandatory = false)
    private String environment;

    @Configuration(value = "org.seedstack.w20.pretty-urls", mandatory = false, defaultValue = "true")
    private boolean prettyUrls;

    @Configuration(value = "org.seedstack.w20.security-provider", mandatory = false, defaultValue = "BasicAuthentication")
    private String securityProvider;

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
                sourceConfiguration.put("prettyUrls", prettyUrls);
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
        if ("seed-w20".equals(fragmentName)) {
            variables.put("securityProvider", securityProvider);
        }
    }
}

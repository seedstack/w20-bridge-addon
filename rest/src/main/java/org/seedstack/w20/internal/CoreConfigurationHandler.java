/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import javax.inject.Inject;
import org.seedstack.seed.Application;
import org.seedstack.seed.Configuration;
import org.seedstack.w20.W20Config;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

class CoreConfigurationHandler implements FragmentConfigurationHandler {
    private final Application application;

    @Configuration
    private W20Config w20Config = new W20Config();

    @Inject
    CoreConfigurationHandler(Application application) {
        this.application = application;
    }

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
                sourceConfiguration.put("prettyUrls", w20Config.isPrettyUrls());
            }

            if ("env".equals(moduleName)) {
                String environment = w20Config.getApplicationInfo().getEnvironment();
                if (!sourceConfiguration.containsKey("type") && environment != null) {
                    sourceConfiguration.put("type", environment);
                }
            }
        }
    }

    @Override
    public void overrideVariables(String fragmentName, Map<String, String> variables) {
        w20Config.getVariables().forEach((k, v) -> {
            if ("*".equals(k) || fragmentName.equals(k)) {
                variables.putAll(v);
            }
        });

        if ("seed-w20".equals(fragmentName)) {
            variables.put("securityProvider", w20Config.getSecurityProvider());
        }
    }
}

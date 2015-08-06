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

import com.google.inject.Inject;
import org.seedstack.seed.core.api.Application;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

import javax.inject.Named;
import javax.servlet.ServletContext;
import java.util.Map;

class GlobalConfigurationHandler implements FragmentConfigurationHandler {
    private final String restPath;
    private final String componentsPath;
    private final String contextPath;

    @Inject(optional = true)
    @Named("SeedWebResourcesPath")
    private String webResourcesPath;

    @Inject
    GlobalConfigurationHandler(ServletContext servletContext, Application application, @Named("SeedRestPath") String restPath) {
        this.restPath = restPath;
        this.contextPath = servletContext.getContextPath();
        this.componentsPath = application.getConfiguration().getString(W20Plugin.W20_PLUGIN_CONFIGURATION_PREFIX + ".components-path");
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
        return null;
    }

    @Override
    public void overrideConfiguration(String fragmentName, String moduleName, Map<String, Object> sourceConfiguration) {
        // nothing to do here
    }

    @Override
    public void overrideVariables(String fragmentName, Map<String, String> variables) {
        // Global variables in all fragments
        variables.put("seed-base-path", PathUtils.removeTrailingSlash(contextPath));
        variables.put("seed-rest-path", PathUtils.buildPath(contextPath, restPath));
        if (webResourcesPath != null) {
            variables.put("seed-webresources-path", PathUtils.buildPath(contextPath, webResourcesPath));
        }
        if (componentsPath == null) {
            if (webResourcesPath != null) {
                variables.put("components-path", PathUtils.buildPath(contextPath, webResourcesPath, "bower_components"));
            }
        } else {
            variables.put("components-path", PathUtils.removeTrailingSlash(componentsPath));
        }
    }
}

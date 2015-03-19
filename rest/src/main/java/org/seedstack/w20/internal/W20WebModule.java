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

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

import java.util.Map;
import java.util.Set;

class W20WebModule extends ServletModule {
    private final Map<String, AvailableFragment> w20Fragments;
    private final Set<Class<? extends FragmentConfigurationHandler>> moduleConfigurationHandlerClasses;
    private final ConfiguredApplication configuredApplication;
    private final boolean masterPageEnabled;

    W20WebModule(boolean masterPageEnabled, Map<String, AvailableFragment> w20Fragments, Set<Class<? extends FragmentConfigurationHandler>> moduleConfigurationHandlerClasses, ConfiguredApplication configuredApplication) {
        this.masterPageEnabled = masterPageEnabled;
        this.w20Fragments = w20Fragments;
        this.moduleConfigurationHandlerClasses = moduleConfigurationHandlerClasses;
        this.configuredApplication = configuredApplication;
    }

    @Override
    protected void configureServlets() {
        install(new W20Module(w20Fragments, moduleConfigurationHandlerClasses, configuredApplication));

        if (masterPageEnabled) {
            bind(MasterpageServlet.class).in(Scopes.SINGLETON);
            serve("/").with(MasterpageServlet.class);
        }
    }
}

/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Providers;
import org.seedstack.w20.FragmentManager;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

import java.util.Map;
import java.util.Set;

class W20Module extends PrivateModule {
    private final Map<String, AvailableFragment> w20Fragments;
    private final Set<Class<? extends FragmentConfigurationHandler>> moduleConfigurationHandlerClasses;
    private final ConfiguredApplication configuredApplication;
    private final boolean masterPageServlet;
    private final boolean prettyUrls;
    private final String restPath;

    W20Module(Map<String, AvailableFragment> w20Fragments, Set<Class<? extends FragmentConfigurationHandler>> moduleConfigurationHandlerClasses, ConfiguredApplication configuredApplication, boolean masterPageServlet, boolean prettyUrls, String restPath) {
        this.w20Fragments = w20Fragments;
        this.moduleConfigurationHandlerClasses = moduleConfigurationHandlerClasses;
        this.configuredApplication = configuredApplication;
        this.masterPageServlet = masterPageServlet;
        this.prettyUrls = prettyUrls;
        this.restPath = restPath;
    }

    @Override
    protected void configure() {
        Multibinder<FragmentConfigurationHandler> multiBinder = Multibinder.newSetBinder(binder(), FragmentConfigurationHandler.class);
        for (Class<? extends FragmentConfigurationHandler> moduleConfigurationHandlerClass : moduleConfigurationHandlerClasses) {
            multiBinder.addBinding().to(moduleConfigurationHandlerClass);
        }

        bind(new TypeLiteral<Map<String, AvailableFragment>>() {
        }).toInstance(W20Module.this.w20Fragments);

        if (W20Module.this.configuredApplication != null) {
            bind(ConfiguredApplication.class).toInstance(W20Module.this.configuredApplication);
        } else {
            bind(ConfiguredApplication.class).toProvider(Providers.of(null));
        }

        bind(FragmentManager.class).to(FragmentManagerImpl.class);
        expose(FragmentManager.class);

        bind(MasterPageBuilder.class);
        expose(MasterPageBuilder.class);

        if (masterPageServlet) {
            bind(MasterpageServlet.class).in(Scopes.SINGLETON);
            expose(MasterpageServlet.class);
        }

        if (prettyUrls) {
            bind(Html5RewriteFilter.class).toInstance(new Html5RewriteFilter(restPath));
            expose(Html5RewriteFilter.class);
        }
    }
}

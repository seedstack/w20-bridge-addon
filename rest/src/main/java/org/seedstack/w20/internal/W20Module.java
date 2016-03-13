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
import com.google.inject.servlet.ServletModule;
import com.google.inject.util.Providers;
import org.seedstack.w20.FragmentManager;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

import java.util.Map;
import java.util.Set;

@W20BridgeConcern
class W20Module extends ServletModule {
    private final Map<String, AvailableFragment> w20Fragments;
    private final Set<Class<? extends FragmentConfigurationHandler>> moduleConfigurationHandlerClasses;
    private final ConfiguredApplication configuredApplication;
    private final boolean masterPageServlet;
    private final boolean prettyUrls;

    W20Module(Map<String, AvailableFragment> w20Fragments, Set<Class<? extends FragmentConfigurationHandler>> moduleConfigurationHandlerClasses, ConfiguredApplication configuredApplication, boolean masterPageServlet, boolean prettyUrls) {
        this.w20Fragments = w20Fragments;
        this.moduleConfigurationHandlerClasses = moduleConfigurationHandlerClasses;
        this.configuredApplication = configuredApplication;
        this.masterPageServlet = masterPageServlet;
        this.prettyUrls = prettyUrls;
    }

    @Override
    protected void configureServlets() {
        install(new PrivateModule() {
            @Override
            protected void configure() {
                Multibinder<FragmentConfigurationHandler> multiBinder = Multibinder.newSetBinder(binder(), FragmentConfigurationHandler.class);
                for (Class<? extends FragmentConfigurationHandler> moduleConfigurationHandlerClass : moduleConfigurationHandlerClasses) {
                    multiBinder.addBinding().to(moduleConfigurationHandlerClass);
                }

                bind(new TypeLiteral<Map<String, AvailableFragment>>() {
                }).toInstance(W20Module.this.w20Fragments);

                bind(FragmentManager.class).to(FragmentManagerImpl.class);

                if (W20Module.this.configuredApplication != null) {
                    bind(ConfiguredApplication.class).toInstance(W20Module.this.configuredApplication);
                } else {
                    bind(ConfiguredApplication.class).toProvider(Providers.<ConfiguredApplication>of(null));
                }

                expose(FragmentManager.class);
            }
        });

        bind(MasterPageBuilder.class);

        if (prettyUrls) {
            bind(Html5RewriteFilter.class).in(Scopes.SINGLETON);
            filter("/*").through(Html5RewriteFilter.class);
        }

        if (masterPageServlet) {
            bind(MasterpageServlet.class).in(Scopes.SINGLETON);
            serve("/").with(MasterpageServlet.class);
        }
    }
}

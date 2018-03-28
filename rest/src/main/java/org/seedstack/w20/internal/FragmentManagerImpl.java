/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;


import org.seedstack.seed.SeedException;
import org.seedstack.w20.ConfiguredFragmentDeclaration;
import org.seedstack.w20.ConfiguredModule;
import org.seedstack.w20.FragmentDeclaration;
import org.seedstack.w20.FragmentManager;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class FragmentManagerImpl implements FragmentManager {
    private final Map<String, AvailableFragment> availableFragments;
    private final Set<FragmentConfigurationHandler> fragmentConfigurationHandlers;
    private final ConfiguredApplication configuredApplication;

    @Inject
    FragmentManagerImpl(Map<String, AvailableFragment> availableFragments, Set<FragmentConfigurationHandler> fragmentConfigurationHandlers, @Nullable ConfiguredApplication configuredApplication) {
        this.availableFragments = availableFragments;
        this.fragmentConfigurationHandlers = fragmentConfigurationHandlers;
        this.configuredApplication = configuredApplication;
    }

    @Override
    public Set<String> getFragmentList() {
        return availableFragments.keySet();
    }

    @Override
    public Collection<FragmentDeclaration> getDeclaredFragments() {
        Collection<FragmentDeclaration> activeFragments = new ArrayList<>();

        // Check for fragments available in this application
        for (Map.Entry<String, AvailableFragment> availableFragmentEntry : availableFragments.entrySet()) {
            ConfiguredFragmentDeclaration configuredFragment = getFragment(availableFragmentEntry.getKey());

            if (Boolean.TRUE.equals(configuredFragment.isIgnore())) {
                continue;
            }

            // Allow FragmentConfigurationHandlers to override module configuration
            if (availableFragmentEntry.getValue().getFragmentDefinition().getModules() != null) {
                for (Map.Entry<String, Module> availableModule : availableFragmentEntry.getValue().getFragmentDefinition().getModules().entrySet()) {
                    ConfiguredModule configuredModule = getFragmentModule(configuredFragment, availableModule.getKey());

                    for (FragmentConfigurationHandler fragmentConfigurationHandler : fragmentConfigurationHandlers) {
                        Boolean fragmentStatus = fragmentConfigurationHandler.overrideFragmentStatus(availableFragmentEntry.getKey());
                        if (fragmentStatus != null) {
                            configuredFragment.setEnabled(fragmentStatus);
                        }

                        Boolean moduleStatus = fragmentConfigurationHandler.overrideModuleStatus(availableFragmentEntry.getKey(), configuredModule.getName());
                        if (moduleStatus != null) {
                            configuredModule.setEnabled(moduleStatus);
                        }

                        if (configuredModule.isEnabled()) {
                            fragmentConfigurationHandler.overrideConfiguration(availableFragmentEntry.getKey(), configuredModule.getName(), configuredModule.getConfiguration());
                        }
                    }

                    if (configuredModule.isEnabled()) {
                        configuredFragment.getModules().put(configuredModule.getName(), configuredModule);
                    }
                }
            }

            if (!configuredFragment.isEnabled()) {
                continue;
            }

            // Allow FragmentConfigurationHandlers to override variables
            for (FragmentConfigurationHandler fragmentConfigurationHandler : fragmentConfigurationHandlers) {
                fragmentConfigurationHandler.overrideVariables(availableFragmentEntry.getKey(), configuredFragment.getVars());
            }

            activeFragments.add(configuredFragment);
        }

        // Add fragments explicitly configured and not available locally
        if (configuredApplication != null) {
            // Allow FragmentConfigurationHandlers to override variables
            configuredApplication.getConfiguredFragments().entrySet().stream().filter(fragmentEntry -> !availableFragments.containsKey(fragmentEntry.getKey())).forEach(fragmentEntry -> {
                ConfiguredFragmentDeclaration configuredFragment = fragmentEntry.getValue();
                configuredFragment.setName(fragmentEntry.getKey());
                configuredFragment.setEnabled(true);

                if (configuredFragment.getModules() != null) {
                    for (Map.Entry<String, ConfiguredModule> moduleEntry : configuredFragment.getModules().entrySet()) {
                        ConfiguredModule configuredModule = moduleEntry.getValue();
                        configuredModule.setName(moduleEntry.getKey());
                        configuredModule.setEnabled(true);
                    }
                } else {
                    configuredFragment.setModules(new HashMap<>());
                }

                if (configuredFragment.getVars() == null) {
                    configuredFragment.setVars(new HashMap<>());
                }

                // Allow FragmentConfigurationHandlers to override variables
                for (FragmentConfigurationHandler fragmentConfigurationHandler : fragmentConfigurationHandlers) {
                    fragmentConfigurationHandler.overrideVariables(fragmentEntry.getKey(), configuredFragment.getVars());
                }

                activeFragments.add(configuredFragment);
            });
        }

        // Add the anonymous fragment
        if (configuredApplication != null && configuredApplication.getAnonymousFragment() != null) {
            activeFragments.add(configuredApplication.getAnonymousFragment());
        }

        return activeFragments;
    }

    private ConfiguredModule getFragmentModule(ConfiguredFragmentDeclaration configuredFragment, String moduleName) {
        ConfiguredModule configuredModule = configuredFragment.getModules().get(moduleName);

        if (configuredModule == null) {
            AvailableFragment availableFragment = availableFragments.get(configuredFragment.getName());
            if (availableFragment != null) {
                Map<String, Module> availableModules = availableFragment.getFragmentDefinition().getModules();
                Module availableModule;
                if (availableModules != null && (availableModule = availableModules.get(moduleName)) != null) {
                    configuredModule = new ConfiguredModule();
                    configuredModule.setName(moduleName);
                    configuredModule.setEnabled(availableModule.isAutoload());

                    if (configuredApplication != null &&
                            configuredApplication.getConfiguredFragments() != null &&
                            configuredApplication.getConfiguredFragments().containsKey(configuredFragment.getName()) &&
                            configuredApplication.getConfiguredFragments().get(configuredFragment.getName()).getModules() != null &&
                            configuredApplication.getConfiguredFragments().get(configuredFragment.getName()).getModules().containsKey(moduleName)) {

                        ConfiguredModule explicitConfiguredModule = configuredApplication.getConfiguredFragments().get(configuredFragment.getName()).getModules().get(moduleName);

                        if (explicitConfiguredModule.getConfiguration() != null) {
                            configuredModule.setConfiguration(explicitConfiguredModule.getConfiguration());
                        }

                        configuredModule.setEnabled(true);
                    }
                } else {
                    throw SeedException.createNew(W20ErrorCode.MODULE_DOES_NOT_EXIST_IN_FRAGMENT)
                            .put("fragment", configuredFragment.getName())
                            .put("module", moduleName);
                }
            } else {
                throw SeedException.createNew(W20ErrorCode.FRAGMENT_NOT_AVAILABLE_IN_APPLICATION)
                        .put("fragment", configuredFragment.getName());
            }
        }

        return configuredModule;
    }

    private ConfiguredFragmentDeclaration getFragment(String fragmentName) {
        AvailableFragment availableFragment = availableFragments.get(fragmentName);
        if (availableFragment == null) {
            throw SeedException.createNew(W20ErrorCode.FRAGMENT_NOT_AVAILABLE_IN_APPLICATION)
                    .put("fragment", fragmentName);
        }

        ConfiguredFragmentDeclaration configuredFragment = new ConfiguredFragmentDeclaration();
        configuredFragment.setName(fragmentName);
        configuredFragment.setEnabled(true);
        configuredFragment.setManifestLocation(availableFragment.getManifestLocation());
        configuredFragment.setModules(new HashMap<>());
        configuredFragment.setVars(new HashMap<>());

        if (configuredApplication != null && configuredApplication.getConfiguredFragments() != null && configuredApplication.getConfiguredFragments().containsKey(fragmentName)) {
            ConfiguredFragmentDeclaration explicitConfiguredFragment = configuredApplication.getConfiguredFragments().get(fragmentName);

            configuredFragment.setPreload(explicitConfiguredFragment.isPreload());
            configuredFragment.setOptional(explicitConfiguredFragment.isOptional());
            configuredFragment.setIgnore(explicitConfiguredFragment.isIgnore());

            if (explicitConfiguredFragment.getModules() != null) {
                for (Map.Entry<String, ConfiguredModule> explicitConfiguredModuleEntry : explicitConfiguredFragment.getModules().entrySet()) {
                    ConfiguredModule explicitConfiguredModule = explicitConfiguredModuleEntry.getValue();
                    explicitConfiguredModule.setEnabled(true);
                    explicitConfiguredModule.setName(explicitConfiguredModuleEntry.getKey());
                    configuredFragment.getModules().put(explicitConfiguredModule.getName(), explicitConfiguredModule);
                }
            }

            if (explicitConfiguredFragment.getRoutes() != null) {
                configuredFragment.setRoutes(explicitConfiguredFragment.getRoutes());
            }

            if (explicitConfiguredFragment.getVars() != null) {
                configuredFragment.setVars(explicitConfiguredFragment.getVars());
            }
        }

        return configuredFragment;
    }
}

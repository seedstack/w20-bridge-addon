/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.rest.application;


import org.seedstack.seed.web.api.WebResourceResolver;
import org.seedstack.w20.api.AnonymousFragmentDeclaration;
import org.seedstack.w20.api.ConfiguredFragmentDeclaration;
import org.seedstack.w20.api.ConfiguredModule;
import org.seedstack.w20.api.FragmentDeclaration;
import org.seedstack.w20.api.FragmentManager;
import org.seedstack.w20.rest.EmptyObjectRepresentation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * This REST resource generates the W20 application configuration.
 *
 * @author adrien.lauer@mpsa.com
 */
@Path("/seed-w20/application/configuration")
public class ApplicationConfigurationResource {
    @Inject
    FragmentManager fragmentManager;

    @Inject
    private WebResourceResolver webResourceResolver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getConfiguration() {
        Map<String, Object> configuredFragmentRepresentations = new HashMap<String, Object>();

        for (FragmentDeclaration declaredFragment : fragmentManager.getDeclaredFragments()) {
            if (declaredFragment instanceof AnonymousFragmentDeclaration) {
                configuredFragmentRepresentations.put("", ((AnonymousFragmentDeclaration)declaredFragment).getContents());
            } else if (declaredFragment instanceof ConfiguredFragmentDeclaration){
                ConfiguredFragmentDeclaration configuredFragment = (ConfiguredFragmentDeclaration)declaredFragment;
                ConfiguredFragmentRepresentation value = new ConfiguredFragmentRepresentation();

                value.setPreload(configuredFragment.isPreload() == null ? true : configuredFragment.isPreload());

                Map<String, Object> modules = new HashMap<String, Object>();
                for (ConfiguredModule configuredModule : configuredFragment.getModules().values()) {
                    Object configuration = configuredModule.getConfiguration();
                    modules.put(configuredModule.getName(), configuration != null ? configuration : new EmptyObjectRepresentation());
                }

                value.setModules(modules);
                value.setVars(configuredFragment.getVars() != null ? configuredFragment.getVars() : new HashMap<String, String>());

                if (configuredFragment.getManifestLocation() != null) {
                    URI resolvedUri = webResourceResolver.resolveURI(configuredFragment.getManifestLocation());

                    if (resolvedUri == null) {
                        throw new IllegalArgumentException("Unable to resolve a web serving path for fragment " + configuredFragment.getName());
                    }

                    configuredFragmentRepresentations.put(resolvedUri.toString(), value);
                } else {
                    configuredFragmentRepresentations.put(configuredFragment.getName(), value);
                }
            }
        }

        return configuredFragmentRepresentations;
    }
}

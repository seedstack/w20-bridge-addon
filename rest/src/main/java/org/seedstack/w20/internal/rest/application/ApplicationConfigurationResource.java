/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.w20.internal.rest.application;

import com.google.inject.Inject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.seedstack.seed.web.WebResourceResolverFactory;
import org.seedstack.w20.AnonymousFragmentDeclaration;
import org.seedstack.w20.ConfiguredFragmentDeclaration;
import org.seedstack.w20.ConfiguredModule;
import org.seedstack.w20.FragmentDeclaration;
import org.seedstack.w20.FragmentManager;
import org.seedstack.w20.internal.MasterPageBuilder;
import org.seedstack.w20.internal.PathUtils;
import org.seedstack.w20.internal.rest.EmptyObjectRepresentation;

/**
 * This REST resource generates the W20 application configuration.
 *
 * @author adrien.lauer@mpsa.com
 */
@Path("/seed-w20/application/configuration")
public class ApplicationConfigurationResource {
    @Inject
    private FragmentManager fragmentManager;

    @Inject
    private MasterPageBuilder masterPageBuilder;

    @Inject(optional = true)
    private WebResourceResolverFactory webResourceResolverFactory;

    @Context
    private ServletContext servletContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getConfiguration() {
        Map<String, Object> configuredFragmentRepresentations = new HashMap<>();

        for (FragmentDeclaration declaredFragment : fragmentManager.getDeclaredFragments()) {
            if (declaredFragment instanceof AnonymousFragmentDeclaration) {
                configuredFragmentRepresentations.put("",
                        ((AnonymousFragmentDeclaration) declaredFragment).getContents());
            } else if (declaredFragment instanceof ConfiguredFragmentDeclaration) {
                ConfiguredFragmentDeclaration configuredFragment = (ConfiguredFragmentDeclaration) declaredFragment;
                ConfiguredFragmentRepresentation value = new ConfiguredFragmentRepresentation();

                value.setPreload(configuredFragment.isPreload() == null ? false : configuredFragment.isPreload());
                value.setIgnore(configuredFragment.isIgnore() == null ? false : configuredFragment.isIgnore());
                value.setOptional(configuredFragment.isOptional() == null ? false : configuredFragment.isOptional());

                Map<String, Object> modules = new HashMap<>();
                for (ConfiguredModule configuredModule : configuredFragment.getModules().values()) {
                    Object configuration = configuredModule.getConfiguration();
                    modules.put(configuredModule.getName(),
                            configuration != null ? configuration : new EmptyObjectRepresentation());
                }
                value.setModules(modules);

                // Copy routes information if any
                Map<String, Object> routes = configuredFragment.getRoutes();
                if (routes != null) {
                    value.setRoutes(routes);
                }

                // Add the default variables and copy explicit variables if any
                Map<String, String> vars = new HashMap<>();
                populateVars(vars);
                Map<String, String> explicitVars = configuredFragment.getVars();
                if (explicitVars != null) {
                    vars.putAll(explicitVars);
                }
                value.setVars(vars);

                if (configuredFragment.getManifestLocation() != null) {
                    // Only includes scanned fragments if a resource resolver is available, otherwise they must be
                    // configured manually
                    if (webResourceResolverFactory != null) {
                        URI resolvedUri = webResourceResolverFactory
                                .createWebResourceResolver(servletContext)
                                .resolveURI(configuredFragment.getManifestLocation());

                        if (resolvedUri == null) {
                            throw new IllegalArgumentException("Unable to resolve a web serving path for fragment " + configuredFragment
                                    .getName());
                        }

                        configuredFragmentRepresentations.put(resolvedUri.toString(), value);
                    }
                } else {
                    configuredFragmentRepresentations.put(configuredFragment.getName(), value);
                }
            }
        }

        return configuredFragmentRepresentations;
    }

    private void populateVars(Map<String, String> vars) {
        String contextPath = servletContext.getContextPath();
        vars.put("seed-base-path", PathUtils.removeTrailingSlash(masterPageBuilder.getBasePath(contextPath)));
        vars.put("seed-base-path-slash", PathUtils.ensureTrailingSlash(masterPageBuilder.getBasePath(contextPath)));
        vars.put("seed-rest-path", PathUtils.removeTrailingSlash(masterPageBuilder.getRestPath(contextPath)));
        vars.put("seed-rest-path-slash", PathUtils.ensureTrailingSlash(masterPageBuilder.getRestPath(contextPath)));
        vars.put("components-path", PathUtils.removeTrailingSlash(masterPageBuilder.getComponentsPath(contextPath)));
        vars.put("components-path-slash",
                PathUtils.ensureTrailingSlash(masterPageBuilder.getComponentsPath(contextPath)));
    }
}

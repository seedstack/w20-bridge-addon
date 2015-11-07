/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal.rest.application;


import com.google.inject.Inject;
import org.seedstack.seed.Application;
import org.seedstack.seed.web.WebResourceResolverFactory;
import org.seedstack.w20.AnonymousFragmentDeclaration;
import org.seedstack.w20.ConfiguredFragmentDeclaration;
import org.seedstack.w20.ConfiguredModule;
import org.seedstack.w20.FragmentDeclaration;
import org.seedstack.w20.FragmentManager;
import org.seedstack.w20.internal.PathUtils;
import org.seedstack.w20.internal.W20Plugin;
import org.seedstack.w20.internal.rest.EmptyObjectRepresentation;

import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
    Application application;

    @Inject
    @Named("SeedRestPath")
    private String restPath;

    @Inject(optional = true)
    @Named("SeedWebResourcesPath")
    private String webResourcesPath;

    @Inject(optional = true)
    private WebResourceResolverFactory webResourceResolverFactory;

    @Context
    private ServletContext servletContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getConfiguration() {
        Map<String, Object> configuredFragmentRepresentations = new HashMap<String, Object>();

        for (FragmentDeclaration declaredFragment : fragmentManager.getDeclaredFragments()) {
            if (declaredFragment instanceof AnonymousFragmentDeclaration) {
                configuredFragmentRepresentations.put("", ((AnonymousFragmentDeclaration) declaredFragment).getContents());
            } else if (declaredFragment instanceof ConfiguredFragmentDeclaration) {
                ConfiguredFragmentDeclaration configuredFragment = (ConfiguredFragmentDeclaration) declaredFragment;
                ConfiguredFragmentRepresentation value = new ConfiguredFragmentRepresentation();

                value.setPreload(configuredFragment.isPreload() == null ? true : configuredFragment.isPreload());

                Map<String, Object> modules = new HashMap<String, Object>();
                for (ConfiguredModule configuredModule : configuredFragment.getModules().values()) {
                    Object configuration = configuredModule.getConfiguration();
                    modules.put(configuredModule.getName(), configuration != null ? configuration : new EmptyObjectRepresentation());
                }
                value.setModules(modules);

                Map<String, String> vars = configuredFragment.getVars() != null ? configuredFragment.getVars() : new HashMap<String, String>();
                populateVars(vars);
                value.setVars(vars);

                if (configuredFragment.getManifestLocation() != null) {
                    // Only includes scanned fragments if a resource resolver is available, otherwise they must be configured manually
                    if (webResourceResolverFactory != null) {
                        URI resolvedUri = webResourceResolverFactory
                                .createWebResourceResolver(servletContext)
                                .resolveURI(configuredFragment.getManifestLocation());

                        if (resolvedUri == null) {
                            throw new IllegalArgumentException("Unable to resolve a web serving path for fragment " + configuredFragment.getName());
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
        String componentsPath = application.getConfiguration().getString(W20Plugin.W20_PLUGIN_CONFIGURATION_PREFIX + ".components-path");

        vars.put("seed-base-path", PathUtils.removeTrailingSlash(contextPath));
        vars.put("seed-rest-path", PathUtils.buildPath(contextPath, restPath));
        if (webResourcesPath != null) {
            vars.put("seed-webresources-path", PathUtils.buildPath(contextPath, webResourcesPath));
        }
        if (componentsPath == null) {
            if (webResourcesPath != null) {
                vars.put("components-path", PathUtils.buildPath(contextPath, webResourcesPath, "bower_components"));
            }
        } else {
            vars.put("components-path", PathUtils.removeTrailingSlash(componentsPath));
        }
    }
}

/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.PluginException;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;
import org.seedstack.seed.core.SeedRuntime;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;
import org.seedstack.seed.rest.internal.RestPlugin;
import org.seedstack.seed.web.spi.FilterDefinition;
import org.seedstack.seed.web.spi.ListenerDefinition;
import org.seedstack.seed.web.spi.ServletDefinition;
import org.seedstack.seed.web.spi.WebProvider;
import org.seedstack.shed.ClassLoaders;
import org.seedstack.w20.W20Config;
import org.seedstack.w20.internal.rest.MasterpageRootResource;
import org.seedstack.w20.spi.FragmentConfigurationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This plugin handles W20 fragment scanning.
 *
 * @author adrien.lauer@mpsa.com
 */
public class W20Plugin extends AbstractSeedPlugin implements WebProvider {
    private static final String FRAGMENTS_REGEX = ".*\\.w20\\.json";
    private static final String W20_CONFIG_NAME = "w20.app.json";

    private final Logger logger = LoggerFactory.getLogger(W20Plugin.class);
    private final Map<String, AvailableFragment> w20Fragments = new HashMap<>();
    private final Set<Class<? extends FragmentConfigurationHandler>> fragmentConfigurationHandlerClasses = new HashSet<>();
    private final ClassLoader classLoader = ClassLoaders.findMostCompleteClassLoader(W20Plugin.class);

    private ConfiguredApplication configuredApplication;
    private ServletContext servletContext;
    private W20Module w20Module;
    private boolean masterPageAsServlet;
    private boolean prettyUrls;

    @Override
    public String name() {
        return "w20";
    }

    @Override
    public String pluginPackageRoot() {
        return "META-INF.resources";
    }

    @Override
    public Collection<Class<?>> dependencies() {
        return Lists.newArrayList(RestPlugin.class);
    }

    @Override
    protected void setup(SeedRuntime seedRuntime) {
        servletContext = seedRuntime.contextAs(ServletContext.class);
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder()
                .subtypeOf(FragmentConfigurationHandler.class)
                .resourcesRegex(FRAGMENTS_REGEX)
                .build();
    }

    @Override
    public InitState initialize(InitContext initContext) {
        if (servletContext == null) {
            logger.info("No servlet context detected, W20 integration disabled");
            return InitState.INITIALIZED;
        }

        final W20Config w20Config = getConfiguration(W20Config.class);
        final RestPlugin restPlugin = initContext.dependency(RestPlugin.class);
        final String restPath = restPlugin.getRestConfig().getPath();

        if (!w20Config.isDisableMasterpage()) {
            if (restPath.isEmpty()) {
                logger.debug("Serving W20 masterpage with a JAX-RS resource");
                restPlugin.addRootResourceVariant(new Variant(MediaType.TEXT_HTML_TYPE, (Locale) null, null), MasterpageRootResource.class);
            } else {
                logger.debug("Serving W20 masterpage with a servlet");
                masterPageAsServlet = true;
            }
        }

        Map<String, Collection<String>> scannedManifestPaths = initContext.mapResourcesByRegex();
        ObjectMapper objectMapper = new ObjectMapper();
        for (String manifestPath : scannedManifestPaths.get(FRAGMENTS_REGEX)) {
            try {
                AvailableFragment availableFragment = new AvailableFragment(manifestPath, objectMapper.readValue(classLoader.getResource(manifestPath), Fragment.class));
                w20Fragments.put(availableFragment.getFragmentDefinition().getId(), availableFragment);
                logger.trace("Detected W20 fragment {} at {}", availableFragment.getFragmentDefinition().getId(), manifestPath);
            } catch (IOException | RuntimeException e) {
                logger.warn("Unable to parse W20 fragment manifest at " + manifestPath, e);
            }
        }
        logger.debug("Detected {} W20 fragment(s)", w20Fragments.size());

        try {
            Enumeration<URL> resources = classLoader.getResources(W20_CONFIG_NAME);
            if (resources.hasMoreElements()) {
                URL confUrl = resources.nextElement();
                if (resources.hasMoreElements()) {
                    throw new PluginException("Found more than one W20 configuration (" + W20_CONFIG_NAME + ") in classpath root");
                }
                configuredApplication = objectMapper.readValue(confUrl, ConfiguredApplication.class);
                logger.info("Loaded W20 configuration " + W20_CONFIG_NAME);
            }
        } catch (IOException e) {
            throw new PluginException("Error reading W20 configuration " + W20_CONFIG_NAME, e);
        }

        Map<Class<?>, Collection<Class<?>>> scannedClassesByParentClass = initContext.scannedSubTypesByParentClass();
        Collection<Class<?>> fragmentConfigurationHandlerCandidateClasses = scannedClassesByParentClass.get(FragmentConfigurationHandler.class);
        if (fragmentConfigurationHandlerCandidateClasses != null) {
            fragmentConfigurationHandlerCandidateClasses.stream().filter(FragmentConfigurationHandler.class::isAssignableFrom).forEach(candidate -> {
                this.fragmentConfigurationHandlerClasses.add(candidate.asSubclass(FragmentConfigurationHandler.class));
            });
        }

        prettyUrls = w20Config.isPrettyUrls();
        logger.debug("Pretty URLs are " + (prettyUrls ? "enabled" : "disabled"));

        w20Module = new W20Module(
                w20Fragments,
                fragmentConfigurationHandlerClasses,
                configuredApplication,
                masterPageAsServlet,
                prettyUrls,
                restPath
        );

        return InitState.INITIALIZED;
    }

    @Override
    public Object nativeUnitModule() {
        return w20Module;
    }

    @Override
    public List<ServletDefinition> servlets() {
        if (masterPageAsServlet) {
            ServletDefinition servletDefinition = new ServletDefinition("w20-masterpage", MasterpageServlet.class);
            servletDefinition.addMappings("");
            return Lists.newArrayList(servletDefinition);
        } else {
            return null;
        }
    }

    @Override
    public List<FilterDefinition> filters() {
        if (prettyUrls) {
            FilterDefinition html5RedirectFilter = new FilterDefinition("w20-html5-redirect", Html5RewriteFilter.class);
            html5RedirectFilter.setPriority(-2000);
            html5RedirectFilter.addMappings(new FilterDefinition.Mapping("/*"));
            html5RedirectFilter.setAsyncSupported(true);
            return Lists.newArrayList(html5RedirectFilter);
        } else {
            return null;
        }
    }

    @Override
    public List<ListenerDefinition> listeners() {
        return null;
    }
}

/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
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
import io.nuun.kernel.core.AbstractPlugin;
import org.apache.commons.configuration.Configuration;
import org.seedstack.seed.SeedRuntime;
import org.seedstack.seed.core.internal.application.ApplicationPlugin;
import org.seedstack.seed.rest.internal.RestPlugin;
import org.seedstack.seed.web.spi.FilterDefinition;
import org.seedstack.seed.web.spi.ListenerDefinition;
import org.seedstack.seed.web.spi.ServletDefinition;
import org.seedstack.seed.web.spi.WebProvider;
import org.seedstack.w20.internal.rest.MasterpageRootResource;
import org.seedstack.w20.spi.FragmentConfigurationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * This plugin handles W20 fragment scanning.
 *
 * @author adrien.lauer@mpsa.com
 */
public class W20Plugin extends AbstractPlugin implements WebProvider {
    public static final String W20_PLUGIN_CONFIGURATION_PREFIX = "org.seedstack.w20";
    public static final String APP_CONF_REGEX = "w20\\.app\\.json";
    public static final String FRAGMENTS_REGEX = ".*\\.w20\\.json";

    private final Logger logger = LoggerFactory.getLogger(W20Plugin.class);
    private final Map<String, AvailableFragment> w20Fragments = new HashMap<String, AvailableFragment>();
    private final Set<Class<? extends FragmentConfigurationHandler>> fragmentConfigurationHandlerClasses = new HashSet<Class<? extends FragmentConfigurationHandler>>();
    private final ClassLoader classLoader = W20Plugin.class.getClassLoader();

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
    public InitState init(InitContext initContext) {
        if (servletContext == null) {
            logger.info("No servlet context detected, W20 integration disabled");
            return InitState.INITIALIZED;
        }

        ApplicationPlugin applicationPlugin = initContext.dependency(ApplicationPlugin.class);
        final RestPlugin restPlugin = initContext.dependency(RestPlugin.class);
        final String restPath = restPlugin.getConfiguration().getRestPath();

        Configuration w20Configuration = applicationPlugin.getApplication().getConfiguration().subset(W20Plugin.W20_PLUGIN_CONFIGURATION_PREFIX);

        if (!w20Configuration.getBoolean("disable-masterpage", false)) {
            if (restPath.isEmpty()) {
                restPlugin.addRootResourceVariant(new Variant(MediaType.TEXT_HTML_TYPE, (Locale) null, null), MasterpageRootResource.class);
            } else {
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
            } catch (Exception e) {
                logger.warn("Unable to parse W20 fragment manifest at " + manifestPath, e);
            }
        }

        logger.debug("Detected {} W20 fragment(s)", w20Fragments.size());

        Collection<String> appConfiguration = scannedManifestPaths.get(APP_CONF_REGEX);
        if (appConfiguration.size() == 1) {
            try {
                String confPath = appConfiguration.iterator().next();
                configuredApplication = objectMapper.readValue(classLoader.getResource(confPath), ConfiguredApplication.class);
                logger.debug("Detected W20 configuration at " + confPath);
            } catch (IOException e) {
                throw new PluginException("Error reading W20 configuration", e);
            }
        } else if (appConfiguration.size() > 1) {
            throw new PluginException("Found more than one W20 configuration: " + appConfiguration);
        }

        Map<Class<?>, Collection<Class<?>>> scannedClassesByParentClass = initContext.scannedSubTypesByParentClass();

        Collection<Class<?>> fragmentConfigurationHandlerCandidateClasses = scannedClassesByParentClass.get(FragmentConfigurationHandler.class);
        if (fragmentConfigurationHandlerCandidateClasses != null) {
            for (Class<?> candidate : fragmentConfigurationHandlerCandidateClasses) {
                if (FragmentConfigurationHandler.class.isAssignableFrom(candidate)) {
                    this.fragmentConfigurationHandlerClasses.add(candidate.asSubclass(FragmentConfigurationHandler.class));
                }
            }
        }

        prettyUrls = w20Configuration.getBoolean("pretty-urls", true);
        logger.debug("Pretty URLs are " + (prettyUrls ? "enabled" : "disabled"));

        w20Module = new W20Module(
                w20Fragments,
                fragmentConfigurationHandlerClasses,
                configuredApplication,
                masterPageAsServlet,
                prettyUrls
        );

        return InitState.INITIALIZED;
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder()
                .subtypeOf(FragmentConfigurationHandler.class)
                .resourcesRegex(FRAGMENTS_REGEX)
                .resourcesRegex(APP_CONF_REGEX)
                .build();
    }

    @Override
    public Collection<Class<?>> requiredPlugins() {
        return Lists.<Class<?>>newArrayList(ApplicationPlugin.class, RestPlugin.class);
    }

    @Override
    public void provideContainerContext(Object containerContext) {
        servletContext = ((SeedRuntime) containerContext).contextAs(ServletContext.class);
    }

    @Override
    public Object nativeUnitModule() {
        return w20Module;
    }

    @Override
    public List<ServletDefinition> servlets() {
        if (masterPageAsServlet) {
            ServletDefinition servletDefinition = new ServletDefinition("w20-masterpage", MasterpageServlet.class);
            servletDefinition.addMappings("/");
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

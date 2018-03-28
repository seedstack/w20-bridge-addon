/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.seedstack.seed.Application;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.SeedException;
import org.seedstack.shed.ClassLoaders;
import org.seedstack.w20.W20Config;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterPageBuilder {
    private static final String MASTER_PAGE_FALLBACK_TEMPLATE = "org/seedstack/w20/masterpage-fallback.html";
    @Inject
    @Named("SeedRestPath")
    private String restPath;
    @Configuration
    private W20Config w20Config = new W20Config();
    @Inject
    private Application application;

    public String build(HttpServletRequest httpServletRequest) {
        ClassLoader classLoader = ClassLoaders.findMostCompleteClassLoader(MasterPageBuilder.class);
        URL masterpageURL = classLoader.getResource(w20Config.getMasterpageTemplate());
        if (masterpageURL == null) {
            masterpageURL = classLoader.getResource(MASTER_PAGE_FALLBACK_TEMPLATE);
        }
        if (masterpageURL == null) {
            throw new RuntimeException("Unable to generate W20 masterpage, template not found");
        }

        Scanner scanner;
        try {
            scanner = new Scanner(new InputStreamReader(masterpageURL.openStream(), StandardCharsets.UTF_8)).useDelimiter("\\A");
        } catch (IOException e) {
            throw SeedException.wrap(e, W20ErrorCode.UNABLE_TO_GENERATE_MASTERPAGE);
        }
        String template = scanner.next();
        scanner.close();

        Map<String, Object> variables = new HashMap<>();
        String contextPath = httpServletRequest.getContextPath();
        W20Config.ApplicationInfo applicationInfo = w20Config.getApplicationInfo();
        variables.put("applicationTitle", StringUtils.isBlank(applicationInfo.getTitle()) ? application.getName() : applicationInfo.getTitle());
        variables.put("applicationSubtitle", applicationInfo.getSubTitle());
        variables.put("applicationVersion", StringUtils.isBlank(applicationInfo.getVersion()) ? application.getVersion() : applicationInfo.getVersion());
        variables.put("timeout", w20Config.getLoadingTimeout());
        variables.put("corsWithCredentials", w20Config.isCorsWithCredentials());
        variables.put("basePath", PathUtils.removeTrailingSlash(getBasePath(contextPath)));
        variables.put("basePathSlash", PathUtils.ensureTrailingSlash(getBasePath(contextPath)));
        variables.put("restPath", PathUtils.removeTrailingSlash(getRestPath(contextPath)));
        variables.put("restPathSlash", PathUtils.ensureTrailingSlash(getRestPath(contextPath)));
        variables.put("componentsPath", PathUtils.removeTrailingSlash(getComponentsPath(contextPath)));
        variables.put("componentsPathSlash", PathUtils.ensureTrailingSlash(getComponentsPath(contextPath)));
        variables.put("prettyUrls", w20Config.isPrettyUrls());

        return replaceTokens(template, variables);

    }

    public String getBasePath(String contextPath) {
        return PathUtils.buildPath("/", contextPath);
    }

    public String getRestPath(String contextPath) {
        return PathUtils.buildPath("/", contextPath, restPath);
    }

    public String getComponentsPath(String contextPath) {
        return Optional.ofNullable(w20Config.getComponentsPath()).orElse(PathUtils.buildPath("/", contextPath, "bower_components"));
    }

    /**
     * Replace ${...} placeholders in a string looking up in a replacement map.
     *
     * @param text         the text to replace.
     * @param replacements the map of replacements.
     * @return the replaced text.
     */
    private String replaceTokens(String text, Map<String, Object> replacements) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            Object replacement = replacements.get(matcher.group(1));
            matcher.appendReplacement(buffer, "");

            if (replacement != null) {
                buffer.append(replacement.toString());
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}

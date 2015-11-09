/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
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
import org.seedstack.seed.core.utils.SeedReflectionUtils;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterPageBuilder {
    @Inject
    @Named("SeedRestPath")
    private String restPath;

    @Inject(optional = true)
    @Named("SeedWebResourcesPath")
    private String webResourcesPath;

    @Configuration(value = "org.seedstack.w20.masterpage-template", defaultValue = "org/seedstack/w20/masterpage.html", mandatory = false)
    private String masterpagePath;

    @Configuration(value = "org.seedstack.w20.application.title", mandatory = false)
    private String applicationTitle;

    @Configuration(value = "org.seedstack.w20.application.subtitle", mandatory = false)
    private String applicationSubtitle;

    @Configuration(value = "org.seedstack.w20.application.version", mandatory = false)
    private String applicationVersion;

    @Configuration(value = "org.seedstack.w20.timeout", mandatory = false, defaultValue = "30")
    private int timeout;

    @Configuration(value = "org.seedstack.w20.cors-with-credentials", mandatory = false, defaultValue = "false")
    private boolean corsWithCredentials;

    @Configuration(value = "org.seedstack.w20.components-path", mandatory = false)
    private String componentsPath;

    @Inject
    private Application application;

    public String build(HttpServletRequest httpServletRequest) {
        URL masterpageURL = SeedReflectionUtils.findMostCompleteClassLoader().getResource(masterpagePath);
        if (masterpageURL == null) {
            throw new RuntimeException("Unable to generate W20 masterpage, template not found");
        }

        Scanner scanner;
        try {
            scanner = new Scanner(masterpageURL.openStream()).useDelimiter("\\A");
        } catch (IOException e) {
            throw SeedException.wrap(e, W20ErrorCode.UNABLE_TO_GENERATE_MASTERPAGE);
        }
        String template = scanner.next();
        scanner.close();

        String contextPath = httpServletRequest.getContextPath();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applicationTitle", StringUtils.isBlank(applicationTitle) ? application.getName() : applicationTitle);
        variables.put("applicationSubtitle", applicationSubtitle);
        variables.put("applicationVersion", StringUtils.isBlank(applicationVersion) ? application.getVersion() : applicationVersion);
        variables.put("timeout", timeout);
        variables.put("corsWithCredentials", corsWithCredentials);
        variables.put("basePath", PathUtils.removeTrailingSlash(contextPath));
        variables.put("restPath", PathUtils.buildPath(contextPath, restPath));
        if (webResourcesPath != null) {
            variables.put("webResourcesPath", PathUtils.buildPath(contextPath, webResourcesPath));
        }
        if (componentsPath == null) {
            if (webResourcesPath != null) {
                variables.put("componentsPath", PathUtils.buildPath(contextPath, webResourcesPath, "bower_components"));
            }
        } else {
            variables.put("componentsPath", PathUtils.removeTrailingSlash(componentsPath));
        }

        return replaceTokens(template, variables);

    }

    /**
     * Replace ${...} placeholders in a string looking up in a replacement map.
     *
     * @param text         the text to replace.
     * @param replacements the map of replacements.
     * @return the replaced text.
     */
    private String replaceTokens(String text, Map<String, Object> replacements) {
        // TODO use a better solution for templating
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

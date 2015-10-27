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
import org.seedstack.seed.core.api.Application;
import org.seedstack.seed.core.api.Configuration;
import org.seedstack.seed.core.utils.SeedStringUtils;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class MasterpageServlet extends HttpServlet {
    private final ClassLoader classLoader = MasterpageServlet.class.getClassLoader();

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getRequestURI().endsWith("/")) {
            String queryString = req.getQueryString();

            if (queryString != null) {
                resp.sendRedirect(req.getRequestURI() + "/" + queryString);
            } else {
                resp.sendRedirect(req.getRequestURI() + "/");
            }
        } else {
            URL masterpageURL = classLoader.getResource(masterpagePath);
            if (masterpageURL == null) {
                throw new RuntimeException("Unable to generate W20 masterpage, template not found");
            }

            Scanner scanner = new Scanner(masterpageURL.openStream()).useDelimiter("\\A");
            String template = scanner.next();
            scanner.close();

            String contextPath = req.getContextPath();

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

            String result = SeedStringUtils.replaceTokens(template, variables);
            resp.setContentLength(result.length());
            resp.setContentType("text/html");
            resp.getWriter().write(result);
        }
    }
}

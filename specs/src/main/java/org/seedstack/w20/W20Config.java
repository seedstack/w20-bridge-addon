/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.seedstack.coffig.Config;

@Config("w20")
public class W20Config {
    private boolean prettyUrls = true;
    private boolean disableMasterpage = false;
    private String masterpageTemplate = "org/seedstack/w20/masterpage.html";
    private int loadingTimeout = 30;
    private boolean corsWithCredentials = false;
    private String componentsPath;
    private String securityProvider = "basicAuth";
    private ApplicationInfo applicationInfo = new ApplicationInfo();
    private Map<String, Map<String, String>> variables = new HashMap<>();

    public boolean isPrettyUrls() {
        return prettyUrls;
    }

    public W20Config setPrettyUrls(boolean prettyUrls) {
        this.prettyUrls = prettyUrls;
        return this;
    }

    public boolean isDisableMasterpage() {
        return disableMasterpage;
    }

    public W20Config setDisableMasterpage(boolean disableMasterpage) {
        this.disableMasterpage = disableMasterpage;
        return this;
    }

    public String getMasterpageTemplate() {
        return masterpageTemplate;
    }

    public W20Config setMasterpageTemplate(String masterpageTemplate) {
        this.masterpageTemplate = masterpageTemplate;
        return this;
    }

    public int getLoadingTimeout() {
        return loadingTimeout;
    }

    public W20Config setLoadingTimeout(int loadingTimeout) {
        this.loadingTimeout = loadingTimeout;
        return this;
    }

    public boolean isCorsWithCredentials() {
        return corsWithCredentials;
    }

    public W20Config setCorsWithCredentials(boolean corsWithCredentials) {
        this.corsWithCredentials = corsWithCredentials;
        return this;
    }

    public String getComponentsPath() {
        return componentsPath;
    }

    public W20Config setComponentsPath(String componentsPath) {
        this.componentsPath = componentsPath;
        return this;
    }

    public String getSecurityProvider() {
        return securityProvider;
    }

    public W20Config setSecurityProvider(String securityProvider) {
        this.securityProvider = securityProvider;
        return this;
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public W20Config setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
        return this;
    }

    public Map<String, Map<String, String>> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    public void setVariable(String fragmentName, String name, String value) {
        this.variables.computeIfAbsent(fragmentName, k -> new HashMap<>()).put(name, value);
    }

    public void setGlobalVariable(String name, String value) {
        this.variables.computeIfAbsent("*", k -> new HashMap<>()).put(name, value);
    }

    @Config("application")
    public static class ApplicationInfo {
        private String title;
        private String subTitle;
        private String version;
        private String environment;

        public String getTitle() {
            return title;
        }

        public ApplicationInfo setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public ApplicationInfo setSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public String getVersion() {
            return version;
        }

        public ApplicationInfo setVersion(String version) {
            this.version = version;
            return this;
        }

        public String getEnvironment() {
            return environment;
        }

        public ApplicationInfo setEnvironment(String environment) {
            this.environment = environment;
            return this;
        }
    }
}

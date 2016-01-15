/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
class Module {
    private String path;
    private boolean autoload;
    private Object configSchema;

    private Map<String, Object> config;

    String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }

    boolean isAutoload() {
        return autoload;
    }

    void setAutoload(boolean autoload) {
        this.autoload = autoload;
    }

    Map<String, Object> getConfig() {
        return config;
    }

    void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    Object getConfigSchema() {
        return configSchema;
    }

    void setConfigSchema(Object configSchema) {
        this.configSchema = configSchema;
    }
}

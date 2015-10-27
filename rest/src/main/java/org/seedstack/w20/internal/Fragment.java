/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
class Fragment {
    private String id;
    private String name;
    private String version;
    private String author;
    private String description;
    private String build;

    private Map<String, Module> modules;

    private Map<String, Object> sections = new HashMap<String, Object>();

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getVersion() {
        return version;
    }

    void setVersion(String version) {
        this.version = version;
    }

    String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getBuild() {
        return build;
    }

    void setBuild(String build) {
        this.build = build;
    }

    Map<String, Module> getModules() {
        return modules;
    }

    void setModules(Map<String, Module> modules) {
        this.modules = modules;
    }

    @JsonAnyGetter
    Map<String, Object> getSections() {
        return sections;
    }

    @JsonAnySetter
    void setSections(String name, Object section) {
        this.sections.put(name, section);
    }
}

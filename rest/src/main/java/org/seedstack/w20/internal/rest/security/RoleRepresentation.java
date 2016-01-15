/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal.rest.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RoleRepresentation {
    /**
     * name
     */
    private String name;

    /**
     * attributes
     */
    private Map<String, List<String>> attributes;

    /**
     * permissions
     */
    private Collection<String[]> permissions;

    /**
     * Getter name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter attributes
     *
     * @return the attributes
     */
    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    /**
     * Setter attributes
     *
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    /**
     * Getter permissions
     *
     * @return the permissions
     */
    public Collection<String[]> getPermissions() {
        return permissions;
    }

    /**
     * Setter permissions
     *
     * @param permissions the permissions to set
     */
    public void setPermissions(Collection<String[]> permissions) {
        this.permissions = permissions;
    }
}

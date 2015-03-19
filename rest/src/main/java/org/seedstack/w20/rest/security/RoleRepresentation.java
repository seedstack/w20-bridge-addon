/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.rest.security;

import java.util.Collection;
import java.util.Map;

public class RoleRepresentation {
    /**
     * name
     */
    private String name;

    /**
     * attributes
     */
    private Map<String, String> attributes;

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
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Setter attributes
     *
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, String> attributes) {
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

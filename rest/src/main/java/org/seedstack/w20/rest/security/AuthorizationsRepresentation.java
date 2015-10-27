/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.rest.security;

import java.util.Collection;
import java.util.Map;

public class AuthorizationsRepresentation {
    /**
     * id
     */
    private String id;

    /**
     * type
     */
    private String type;

    /**
     * pricipals
     */
    private Map<String, String> principals;

    /**
     * roles
     */
    private Collection<RoleRepresentation> roles;

    /**
     * permissions
     */
    private Collection<String[]> permissions;

    /**
     * Getter id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter id
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter type
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter type
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter principals
     *
     * @return the principals
     */
    public Map<String, String> getPrincipals() {
        return principals;
    }

    /**
     * Setter principals
     *
     * @param principals the principals to set
     */
    public void setPrincipals(Map<String, String> principals) {
        this.principals = principals;
    }

    /**
     * Getter roles
     *
     * @return the roles
     */
    public Collection<RoleRepresentation> getRoles() {
        return roles;
    }

    /**
     * Setter roles
     *
     * @param roles the roles to set
     */
    public void setRoles(Collection<RoleRepresentation> roles) {
        this.roles = roles;
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

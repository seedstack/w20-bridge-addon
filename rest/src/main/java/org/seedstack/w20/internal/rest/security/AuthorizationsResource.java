/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal.rest.security;


import org.seedstack.seed.security.Permission;
import org.seedstack.seed.security.Role;
import org.seedstack.seed.security.Scope;
import org.seedstack.seed.security.SecuritySupport;
import org.seedstack.seed.security.principals.Principals;
import org.seedstack.seed.security.principals.SimplePrincipalProvider;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/seed-w20/security/authorizations")
public class AuthorizationsResource {

    @Inject
    private SecuritySupport securitySupport;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthenticatedSubjectAuthorizations() {
        if (!securitySupport.isAuthenticated()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // Principals
        Map<String, String> principals = new HashMap<String, String>();
        for (SimplePrincipalProvider simplePrincipalProvider : securitySupport.getSimplePrincipals()) {
            principals.put(simplePrincipalProvider.getName(), simplePrincipalProvider.getValue());
        }

        // Roles
        List<RoleRepresentation> roleRepresentations = new ArrayList<RoleRepresentation>();

        for (Role role : securitySupport.getRoles()) {
            List<String[]> rolePermissions = new ArrayList<String[]>();
            Map<String, List<String>> roleAttributes = new HashMap<String, List<String>>();
            for (Scope scope : role.getScopes()) {
                String attributeName = scope.getName();
                List<String> scopeValues = roleAttributes.get(attributeName);
                if (scopeValues == null) {
                    scopeValues = new ArrayList<String>();
                    roleAttributes.put(attributeName, scopeValues);
                }

                scopeValues.add(scope.getValue());
            }
            for (Permission corePermission : role.getPermissions()) {
                rolePermissions.add(corePermission.getPermission().split(":"));
            }
            RoleRepresentation roleRepresentation = new RoleRepresentation();
            roleRepresentation.setName(role.getName());
            roleRepresentation.setPermissions(rolePermissions);
            roleRepresentation.setAttributes(roleAttributes);
            roleRepresentations.add(roleRepresentation);
        }
        // Individual permissions
        List<String[]> individualPermissions = new ArrayList<String[]>();

        AuthorizationsRepresentation authorizationsRepresentation = new AuthorizationsRepresentation();
        authorizationsRepresentation.setId(securitySupport.getSimplePrincipalByName(Principals.IDENTITY).getValue());
        authorizationsRepresentation.setType("user");
        authorizationsRepresentation.setRoles(roleRepresentations);
        authorizationsRepresentation.setPrincipals(principals);
        authorizationsRepresentation.setPermissions(individualPermissions);

        return Response.ok(authorizationsRepresentation).build();
    }
}

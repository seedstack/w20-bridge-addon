/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.rest.security;


import org.seedstack.seed.security.api.SecuritySupport;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/seed-w20/security/basic-authentication")
public class BasicAuthenticationResource {
    @Inject
    SecuritySupport securitySupport;

    @GET
    public Response authenticate() {
        if (!securitySupport.isAuthenticated()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    public Response deauthenticate() {
        securitySupport.logout();

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}

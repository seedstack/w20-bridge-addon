/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal.rest;

import org.seedstack.seed.rest.spi.RootResource;
import org.seedstack.w20.internal.MasterPageBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class MasterpageRootResource implements RootResource {
    @Inject
    private MasterPageBuilder masterPageBuilder;

    @Override
    public Response buildResponse(HttpServletRequest httpServletRequest, UriInfo uriInfo) {
        return Response.ok(masterPageBuilder.build(httpServletRequest)).type(MediaType.TEXT_HTML_TYPE).build();
    }
}

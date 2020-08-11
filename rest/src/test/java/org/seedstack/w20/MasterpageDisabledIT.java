/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.testing.ConfigurationProperty;
import org.seedstack.seed.testing.junit4.SeedITRunner;
import org.seedstack.seed.undertow.LaunchWithUndertow;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@RunWith(SeedITRunner.class)
@LaunchWithUndertow
@ConfigurationProperty(name = "w20.disableMasterpage", value = "true")
public class MasterpageDisabledIT {
    @Configuration("runtime.web.baseUrl")
    private String baseUrl;

    @Test
    @Ignore("we get a 403 instead of a 404... why ?")
    public void masterpage_is_disabled() {
        given()
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
                .expect()
                .statusCode(404)
                .when()
                .get(baseUrl);
    }
}

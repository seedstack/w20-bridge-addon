/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.testing.ConfigurationProperty;
import org.seedstack.seed.testing.junit4.SeedITRunner;
import org.seedstack.seed.undertow.LaunchWithUndertow;

@RunWith(SeedITRunner.class)
@LaunchWithUndertow
@ConfigurationProperty(name = "web.static", value = "false")
public class NoSeedWebResourcesIT {
    @Configuration("runtime.web.baseUrl")
    private String baseUrl;

    @Test
    public void non_resolvable_fragments_are_omitted() {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(response).doesNotContain("\"" + baseUrl + "seed-w20/seed-w20.w20.json\"");
    }
}

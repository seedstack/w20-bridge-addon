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

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class NoSeedWebResourcesIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addAsResource("no-resources.yaml", "META-INF/configuration/no-resources.yaml");
    }

    @Test
    @RunAsClient
    public void non_resolvable_fragments_are_omitted(@ArquillianResource URL baseUrl) {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(response).doesNotContain("\"" + baseUrl.getPath() + "seed-w20/seed-w20.w20.json\"");
    }
}

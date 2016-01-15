/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.seedstack.seed.it.AbstractSeedWebIT;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.net.URL;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class MasterpageDisabledIT extends AbstractSeedWebIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addAsResource("masterpage-disabled.props", "META-INF/configuration/masterpage-disabled.props").addAsWebResource("index.html", "index.html");
    }

    @Test
    @RunAsClient
    public void masterpage_is_disabled(@ArquillianResource URL baseUrl) {
        String result = given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString())
                .asString();
        assertThat(result).contains("<h1>Custom index</h1>");
    }
}

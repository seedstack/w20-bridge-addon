/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
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
import org.mockito.internal.matchers.StartsWith;
import org.seedstack.seed.it.AbstractSeedWebIT;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.net.URL;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class W20BridgeIT extends AbstractSeedWebIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).setWebXML("WEB-INF/web.xml");
    }

    @Test
    @RunAsClient
    public void masterpage_is_served_without_trailing_slash(@ArquillianResource URL baseUrl) {
        String url = baseUrl.toString();
        url = url.substring(0, url.length() - 1);
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
                .expect()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, new StartsWith(MediaType.TEXT_HTML))
                .when()
                .get(url);
    }

    @Test
    @RunAsClient
    public void masterpage_is_served_with_trailing_slash(@ArquillianResource URL baseUrl) {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
                .expect()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, new StartsWith(MediaType.TEXT_HTML))
                .when()
                .get(baseUrl.toString());
    }

    @Test
    @RunAsClient
    public void detected_fragments_are_resolved(@ArquillianResource URL baseUrl) {
        String response = given().auth().basic("ThePoltergeist", "bouh").expect().statusCode(200).when().get(baseUrl.toString() + "seed-w20/application/configuration").getBody().asString();
        assertThat(response).contains("\"" + baseUrl.getPath() + "seed-w20/seed-w20.w20.json\"");
    }

    @Test
    @RunAsClient
    public void anonymous_fragment_is_preserved(@ArquillianResource URL baseUrl) {
        String response = given().auth().basic("ThePoltergeist", "bouh").expect().statusCode(200).when().get(baseUrl.toString() + "seed-w20/application/configuration").getBody().asString();
        assertThat(response).contains("\"\":{\"routes\":{\"/\":{\"templateUrl\":\"non-existent-template.html\"}}}");
    }
}

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WithPrefixesIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addAsResource("with-prefixes.yaml", "META-INF/configuration/with-prefixes.yaml");
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
                .header(HttpHeaders.CONTENT_TYPE, Matchers.startsWith(MediaType.TEXT_HTML))
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
                .header(HttpHeaders.CONTENT_TYPE, Matchers.startsWith(MediaType.TEXT_HTML))
                .when()
                .get(baseUrl.toString());
    }

    @Test
    @RunAsClient
    public void json_home_is_served_on_rest_root(@ArquillianResource URL baseUrl) {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .expect()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, Matchers.startsWith(MediaType.APPLICATION_JSON))
                .when()
                .get(baseUrl.toString() + "rest/");
    }

    @Test
    @RunAsClient
    public void wrong_rest_url_is_not_redirected(@ArquillianResource URL baseUrl) {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(404)
                .when()
                .get(baseUrl.toString() + "rest/wrong");
    }

    @Test
    @RunAsClient
    public void paths_are_correctly_built(@ArquillianResource URL baseUrl) {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "rest/seed-w20/application/configuration")
                .getBody()
                .asString();
        String prefix = baseUrl.toString()
                .substring((baseUrl.getProtocol() + "://" + baseUrl.getHost() + ":" + baseUrl.getPort()).length(),
                        baseUrl.toString().length() - 1);
        assertThat(response).contains("\"components-path\":\"" + prefix + "/bower_components\"");
        assertThat(response).contains("\"components-path-slash\":\"" + prefix + "/bower_components/\"");
        assertThat(response).contains("\"seed-base-path\":\"" + prefix + "\"");
        assertThat(response).contains("\"seed-base-path-slash\":\"" + prefix + "/\"");
        assertThat(response).contains("\"seed-rest-path\":\"" + prefix + "/rest\"");
        assertThat(response).contains("\"seed-rest-path-slash\":\"" + prefix + "/rest/\"");
    }
}

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

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.testing.ConfigurationProperty;
import org.seedstack.seed.testing.junit4.SeedITRunner;
import org.seedstack.seed.undertow.LaunchWithUndertow;

@RunWith(SeedITRunner.class)
@LaunchWithUndertow
@ConfigurationProperty(name = "rest.path", value = "/rest")
public class WithPrefixesIT {
    @Configuration("runtime.web.baseUrl")
    private String baseUrl;
    @Configuration("runtime.web.baseUrlSlash")
    private String baseUrlSlash;
    @Configuration("runtime.web.server.protocol")
    private String protocol;
    @Configuration("runtime.web.server.host")
    private String host;
    @Configuration("runtime.web.server.port")
    private int port;

    @Test
    public void masterpage_is_served_without_trailing_slash() {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
                .expect()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, Matchers.startsWith(MediaType.TEXT_HTML))
                .when()
                .get(baseUrl);
    }

    @Test
    public void masterpage_is_served_with_trailing_slash() {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
                .expect()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, Matchers.startsWith(MediaType.TEXT_HTML))
                .when()
                .get(baseUrlSlash);
    }

    @Test
    public void json_home_is_served_on_rest_root() {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .expect()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, Matchers.startsWith(MediaType.APPLICATION_JSON))
                .when()
                .get(baseUrl + "/rest/");
    }

    @Test
    public void wrong_rest_url_is_not_redirected() {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(404)
                .when()
                .get(baseUrl + "/rest/wrong");
    }

    @Test
    public void paths_are_correctly_built() {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/rest/seed-w20/application/configuration")
                .getBody()
                .asString();
        String prefix = baseUrl.substring((protocol + "://" + host + ":" + port).length());
        assertThat(response).contains("\"components-path\":\"" + prefix + "/node_modules\"");
        assertThat(response).contains("\"components-path-slash\":\"" + prefix + "/node_modules/\"");
        assertThat(response).contains("\"seed-base-path\":\"" + prefix + "\"");
        assertThat(response).contains("\"seed-base-path-slash\":\"" + prefix + "/\"");
        assertThat(response).contains("\"seed-rest-path\":\"" + prefix + "/rest\"");
        assertThat(response).contains("\"seed-rest-path-slash\":\"" + prefix + "/rest/\"");
    }
}

/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.testing.junit4.SeedITRunner;
import org.seedstack.seed.undertow.LaunchWithUndertow;
import org.seedstack.w20.ConfiguredFragmentDeclaration;

@RunWith(SeedITRunner.class)
@LaunchWithUndertow
public class W20BridgeIT {
    @Configuration("runtime.web.baseUrl")
    private String baseUrl;
    @Configuration("runtime.web.server.protocol")
    private String protocol;
    @Configuration("runtime.web.server.host")
    private String host;
    @Configuration("runtime.web.server.port")
    private int port;
    @Configuration("runtime.web.servlet.contextPath")
    private String contextPath;

    @Test
    public void masterpage_is_correctly_served() {
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
    public void redirection_to_masterpage_is_working() {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
                .expect()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, Matchers.startsWith(MediaType.TEXT_HTML))
                .when()
                .get(baseUrl + "/subpage");
    }

    @Test
    public void detected_fragments_are_resolved() {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(response).contains("\"/" + contextPath + "seed-w20/seed-w20.w20.json\"");
    }

    @Test
    public void routes_are_preserved() {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(response).contains("\"routes\":{\"route1\":{\"hidden\":true,\"category\":\"a.b.c\"}}");
        assertThat(response).contains("\"routes\":{\"route2\":{\"hidden\":false,\"category\":\"x.y.z\"}}");
    }

    @Test
    public void anonymous_fragment_is_preserved() {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(response).contains("\"\":{\"routes\":{\"/\":{\"templateUrl\":\"non-existent-template.html\"}}}");
    }

    @Test
    public void paths_are_correctly_built() {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/seed-w20/application/configuration")
                .getBody()
                .asString();
        String prefix = baseUrl.substring((protocol + "://" + host + ":" + port).length());
        assertThat(response).contains("\"components-path\":\"" + prefix + "/node_modules\"");
        assertThat(response).contains("\"components-path-slash\":\"" + prefix + "/node_modules/\"");
        assertThat(response).contains("\"seed-base-path\":\"" + prefix + "\"");
        assertThat(response).contains("\"seed-base-path-slash\":\"" + prefix + "/\"");
        assertThat(response).contains("\"seed-rest-path\":\"" + prefix + "\"");
        assertThat(response).contains("\"seed-rest-path-slash\":\"" + prefix + "/\"");

    }

    @Test
    public void fragment_ignore() throws IOException {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(getFragment(response, "/" + contextPath + "seed-w20/seed-w20.w20.json").isIgnore()).isFalse();
        assertThat(getFragment(response, "/" + contextPath + "ignored-fragment.w20.json")).isNull();
        assertThat(getFragment(response, "ignored-external-fragment.w20.json").isIgnore()).isTrue();
    }

    @Test
    public void fragment_optional() throws IOException {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(getFragment(response, "/" + contextPath + "seed-w20/seed-w20.w20.json").isOptional()).isFalse();
        assertThat(getFragment(response, "/" + contextPath + "optional-fragment.w20.json").isOptional()).isTrue();
        assertThat(getFragment(response, "optional-external-fragment.w20.json").isOptional()).isTrue();
    }

    @Test
    public void variables() throws IOException {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl + "/seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(getFragment(response, "/" + contextPath + "seed-w20/seed-w20.w20.json").getVars()).contains(
                entry("var1", "value1")
        );
        assertThat(getFragment(response, "/" + contextPath + "seed-w20/seed-w20.w20.json").getVars()).doesNotContain(
                entry("var2", "value2")
        );
        assertThat(getFragment(response, "/" + contextPath + "fragment-with-routes.w20.json").getVars()).contains(
                entry("var1", "value1"),
                entry("var2", "value2")
        );
    }

    private ConfiguredFragmentDeclaration getFragment(String response, String key) throws IOException {
        ConfiguredApplication configuredApplication = new ObjectMapper().readValue(response,
                ConfiguredApplication.class);
        return configuredApplication.getConfiguredFragments().get(key);
    }
}

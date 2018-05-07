/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
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
import org.seedstack.w20.ConfiguredFragmentDeclaration;

@RunWith(Arquillian.class)
public class W20BridgeIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class);
    }

    @Test
    @RunAsClient
    public void masterpage_is_correctly_served(@ArquillianResource URL baseUrl) {
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
    public void redirection_to_masterpage_is_working(@ArquillianResource URL baseUrl) {
        given()
                .auth().basic("ThePoltergeist", "bouh")
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
                .expect()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, Matchers.startsWith(MediaType.TEXT_HTML))
                .when()
                .get(baseUrl.toString() + "subpage");
    }

    @Test
    @RunAsClient
    public void detected_fragments_are_resolved(@ArquillianResource URL baseUrl) {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(response).contains("\"" + baseUrl.getPath() + "seed-w20/seed-w20.w20.json\"");
    }

    @Test
    @RunAsClient
    public void routes_are_preserved(@ArquillianResource URL baseUrl) {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(response).contains("\"routes\":{\"route1\":{\"hidden\":true,\"category\":\"a.b.c\"}}");
        assertThat(response).contains("\"routes\":{\"route2\":{\"hidden\":false,\"category\":\"x.y.z\"}}");
    }

    @Test
    @RunAsClient
    public void anonymous_fragment_is_preserved(@ArquillianResource URL baseUrl) {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(response).contains("\"\":{\"routes\":{\"/\":{\"templateUrl\":\"non-existent-template.html\"}}}");
    }

    @Test
    @RunAsClient
    public void paths_are_correctly_built(@ArquillianResource URL baseUrl) {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "seed-w20/application/configuration")
                .getBody()
                .asString();
        String prefix = baseUrl.toString()
                .substring((baseUrl.getProtocol() + "://" + baseUrl.getHost() + ":" + baseUrl.getPort()).length(),
                        baseUrl.toString().length() - 1);
        assertThat(response).contains("\"components-path\":\"" + prefix + "/node_modules\"");
        assertThat(response).contains("\"components-path-slash\":\"" + prefix + "/node_modules/\"");
        assertThat(response).contains("\"seed-base-path\":\"" + prefix + "\"");
        assertThat(response).contains("\"seed-base-path-slash\":\"" + prefix + "/\"");
        assertThat(response).contains("\"seed-rest-path\":\"" + prefix + "\"");
        assertThat(response).contains("\"seed-rest-path-slash\":\"" + prefix + "/\"");

    }

    @Test
    @RunAsClient
    public void fragment_ignore(@ArquillianResource URL baseUrl) throws IOException {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(getFragment(response, baseUrl.getPath() + "seed-w20/seed-w20.w20.json").isIgnore()).isFalse();
        assertThat(getFragment(response, baseUrl.getPath() + "ignored-fragment.w20.json")).isNull();
        assertThat(getFragment(response, "ignored-external-fragment.w20.json").isIgnore()).isTrue();
    }

    @Test
    @RunAsClient
    public void fragment_optional(@ArquillianResource URL baseUrl) throws IOException {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(getFragment(response, baseUrl.getPath() + "seed-w20/seed-w20.w20.json").isOptional()).isFalse();
        assertThat(getFragment(response, baseUrl.getPath() + "optional-fragment.w20.json").isOptional()).isTrue();
        assertThat(getFragment(response, "optional-external-fragment.w20.json").isOptional()).isTrue();
    }

    @Test
    @RunAsClient
    public void variables(@ArquillianResource URL baseUrl) throws IOException {
        String response = given().auth()
                .basic("ThePoltergeist", "bouh")
                .expect()
                .statusCode(200)
                .when()
                .get(baseUrl.toString() + "seed-w20/application/configuration")
                .getBody()
                .asString();
        assertThat(getFragment(response, baseUrl.getPath() + "seed-w20/seed-w20.w20.json").getVars()).contains(
                entry("var1", "value1")
        );
        assertThat(getFragment(response, baseUrl.getPath() + "seed-w20/seed-w20.w20.json").getVars()).doesNotContain(
                entry("var2", "value2")
        );
        assertThat(getFragment(response, baseUrl.getPath() + "fragment-with-routes.w20.json").getVars()).contains(
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

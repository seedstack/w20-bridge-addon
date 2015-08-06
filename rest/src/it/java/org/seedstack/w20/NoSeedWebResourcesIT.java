/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
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

import java.net.URL;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class NoSeedWebResourcesIT extends AbstractSeedWebIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addAsResource("org.seedstack.w20.no-resources.props", "META-INF/configuration/org.seedstack.w20.no-resources.props").setWebXML("WEB-INF/web.xml");
    }

    @Test
    @RunAsClient
    public void non_resolvable_fragments_are_omitted(@ArquillianResource URL baseUrl) {
        String response = given().auth().basic("ThePoltergeist", "bouh").expect().statusCode(200).when().get(baseUrl.toString() + "rest/seed-w20/application/configuration").getBody().asString();
        assertThat(response).doesNotContain("\"" + baseUrl.getPath() + "resources/seed-w20/seed-w20.w20.json\"");
    }
}

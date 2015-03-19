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

public class ApplicationIT extends AbstractSeedWebIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).setWebXML("WEB-INF/web.xml");
    }

    @Test
    @RunAsClient
    public void masterpage_is_served_without_trailing_slash(@ArquillianResource URL baseUrl) {
        String url = baseUrl.toString();
        url = url.substring(0, url.length() - 1);
        given().auth().basic("ThePoltergeist", "bouh").expect().statusCode(200).when().get(url);
    }

    @Test
    @RunAsClient
    public void masterpage_is_served_with_trailing_slash(@ArquillianResource URL baseUrl) {
        given().auth().basic("ThePoltergeist", "bouh").expect().statusCode(200).when().get(baseUrl.toString());
    }
}

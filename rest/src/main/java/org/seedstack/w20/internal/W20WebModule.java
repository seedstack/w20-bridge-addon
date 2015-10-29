/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import org.seedstack.w20.spi.FragmentConfigurationHandler;

import java.util.Map;
import java.util.Set;

class W20WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        bind(MasterpageServlet.class).in(Scopes.SINGLETON);
        serve("/").with(MasterpageServlet.class);
    }
}

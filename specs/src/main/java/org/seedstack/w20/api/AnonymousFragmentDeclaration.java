/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.api;

import java.util.Map;

/**
 * This class represents an anonymous fragment declaration.
 *
 * @author adrien.lauer@mpsa.com
 */
public class AnonymousFragmentDeclaration implements FragmentDeclaration {
    private final Map<String, Object> contents;

    public AnonymousFragmentDeclaration(Map<String, Object> contents) {
        this.contents = contents;
    }

    public Map<String, Object> getContents() {
        return contents;
    }

    @Override
    public String getName() {
        return "";
    }
}

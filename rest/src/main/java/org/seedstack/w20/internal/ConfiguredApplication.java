/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seedstack.w20.api.AnonymousFragmentDeclaration;
import org.seedstack.w20.api.ConfiguredFragmentDeclaration;

import java.util.HashMap;
import java.util.Map;

class ConfiguredApplication {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, ConfiguredFragmentDeclaration> configuredFragments = new HashMap<String, ConfiguredFragmentDeclaration>();
    private AnonymousFragmentDeclaration anonymousFragment;

    @JsonAnyGetter
    Map<String, ConfiguredFragmentDeclaration> getConfiguredFragments() {
        return configuredFragments;
    }

    @JsonAnySetter
    void putConfiguredFragment(String url, Map<String, Object> value) {
        if (url.isEmpty()) {
            anonymousFragment = new AnonymousFragmentDeclaration(value);
        } else {
            configuredFragments.put(url, objectMapper.convertValue(value, ConfiguredFragmentDeclaration.class));
        }
    }

    AnonymousFragmentDeclaration getAnonymousFragment() {
        return anonymousFragment;
    }
}

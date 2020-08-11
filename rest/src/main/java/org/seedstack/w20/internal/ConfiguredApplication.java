/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seedstack.w20.AnonymousFragmentDeclaration;
import org.seedstack.w20.ConfiguredFragmentDeclaration;

import java.util.HashMap;
import java.util.Map;

class ConfiguredApplication {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, ConfiguredFragmentDeclaration> configuredFragments = new HashMap<>();
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

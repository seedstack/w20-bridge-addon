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
import org.seedstack.w20.api.ConfiguredFragment;

import java.util.HashMap;
import java.util.Map;

class ConfiguredApplication {
    private Map<String, ConfiguredFragment> configuredFragments = new HashMap<String, ConfiguredFragment>();

    @JsonAnyGetter
    Map<String, ConfiguredFragment> getConfiguredFragments() {
        return configuredFragments;
    }

    @JsonAnySetter
    void putConfiguredFragment(String url, ConfiguredFragment configuredFragment) {
        configuredFragments.put(url, configuredFragment);
    }

    @JsonAnySetter
    void setConfiguredFragments(Map<String, ConfiguredFragment> configuredFragments) {
        this.configuredFragments = configuredFragments;
    }
}

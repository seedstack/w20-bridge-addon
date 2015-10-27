/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

class AvailableFragment {
    private String manifestLocation;
    private Fragment fragmentDefinition;

    AvailableFragment(String manifestLocation, Fragment fragmentDefinition) {
        this.manifestLocation = manifestLocation;
        this.fragmentDefinition = fragmentDefinition;
    }

    String getManifestLocation() {
        return manifestLocation;
    }

    void setManifestLocation(String manifestLocation) {
        this.manifestLocation = manifestLocation;
    }

    Fragment getFragmentDefinition() {
        return fragmentDefinition;
    }

    void setFragmentDefinition(Fragment fragmentDefinition) {
        this.fragmentDefinition = fragmentDefinition;
    }
}

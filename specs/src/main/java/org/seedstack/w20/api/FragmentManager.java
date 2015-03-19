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

import java.util.Collection;
import java.util.Set;

/**
 * The fragment manager allows to programatically alter the managed W20 configuration.
 *
 * @author adrien.lauer@mpsa.com
 */
public interface FragmentManager {
    /**
     * @return the fragment identifiers list.
     */
    Set<String> getFragmentList();

    /**
     * @return the collection of configured fragments.
     */
    Collection<ConfiguredFragment> getConfiguredFragments();
}

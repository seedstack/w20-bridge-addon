/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;

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
     * @return the collection of declared fragments in the managed W20 configuration.
     */
    Collection<FragmentDeclaration> getDeclaredFragments();
}

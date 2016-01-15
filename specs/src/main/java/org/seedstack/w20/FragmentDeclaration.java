/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;

import java.io.Serializable;

/**
 * Common interface to anonymous and configured fragments declarations.
 *
 * @author adrien.lauer@mpsa.com
 */
public interface FragmentDeclaration extends Serializable {
    /**
     * Gets the fragment name.
     *
     * @return the name of the fragment.
     */
    String getName();
}

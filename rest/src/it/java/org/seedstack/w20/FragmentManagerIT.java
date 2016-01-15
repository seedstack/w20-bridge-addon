/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.seedstack.seed.it.AbstractSeedIT;

import javax.inject.Inject;

public class FragmentManagerIT extends AbstractSeedIT {
    @Inject
    FragmentManager fragmentManager;

    @Test
    public void w20_integration_is_disabled_when_not_in_a_web_context() {
        Assertions.assertThat(fragmentManager).isNotNull();
        Assertions.assertThat(fragmentManager.getFragmentList().isEmpty()).isTrue();
    }
}

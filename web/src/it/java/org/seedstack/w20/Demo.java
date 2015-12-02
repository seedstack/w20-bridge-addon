/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20;

import org.seedstack.seed.core.SeedMain;

public class Demo {
    public static void main(String[] args) {
        try {
            SeedMain.getLauncher().launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

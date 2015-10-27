/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;


import org.seedstack.seed.core.api.ErrorCode;

enum W20ErrorCode implements ErrorCode {
    FRAGMENT_NOT_AVAILABLE_IN_APPLICATION,
    MODULE_DOES_NOT_EXIST_IN_FRAGMENT
}

/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

/**
 * W20 path utils.
 *
 * @author adrien.lauer@mpsa.com
 */
public final class PathUtils {
    private PathUtils() {
    }

    public static String removeTrailingSlash(String path) {
        if (!path.equals("/") && path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        } else {
            return path;
        }
    }

    public static String ensureTrailingSlash(String path) {
        if (!path.equals("/") && !path.endsWith("/")) {
            return path + "/";
        } else {
            return path;
        }
    }

    public static String buildPath(String first, String... parts) {
        String result = first;

        for (String part : parts) {
            if (result.endsWith("/") && part.startsWith("/")) {
                result += part.substring(1);
            } else if (!result.endsWith("/") && !part.startsWith("/")) {
                result += "/" + part;
            } else {
                result += part;
            }
        }

        return result;
    }
}

/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
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

    /**
     * Remove the trailing slash from the specified path if present. Warning, the slash will be removed even if it is
     * the only character of the path, making it an empty relative path.
     *
     * @param path the path to modify.
     * @return the altered path.
     */
    public static String removeTrailingSlash(String path) {
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        } else {
            return path;
        }
    }

    /**
     * Add a trailing slash to the specified path if missing. Warning, a slash will be appended to the path even if it
     * is empty, making it a root absolute path.
     *
     * @param path the path to modify.
     * @return the altered path.
     */
    public static String ensureTrailingSlash(String path) {
        if (!path.endsWith("/")) {
            return path + "/";
        } else {
            return path;
        }
    }

    /**
     * Add a leading slash to the specified path if missing. Warning, a slash will be prepended to the path even if it
     * is empty, making it a root absolute path.
     *
     * @param path the path to modify.
     * @return the altered path.
     */
    public static String ensureLeadingSlash(String path) {
        if (!path.startsWith("/")) {
            return "/" + path;
        } else {
            return path;
        }
    }

    /**
     * Concatenate path parts into a full path, taking care of extra or missing slashes.
     *
     * @param first the first part.
     * @param parts the additional parts.
     * @return the concatenated path.
     */
    public static String buildPath(String first, String... parts) {
        String result = first;

        for (String part : parts) {
            if (result.isEmpty()) {
                if (part.startsWith("/")) {
                    result += part.substring(1);
                } else {
                    result += part;
                }
            } else {
                if (result.endsWith("/") && part.startsWith("/")) {
                    result += part.substring(1);
                } else if (!result.endsWith("/") && !part.startsWith("/") && !part.isEmpty()) {
                    result += "/" + part;
                } else {
                    result += part;
                }
            }
        }

        return result;
    }
}

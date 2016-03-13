/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class Html5RewriteFilter implements Filter {
    public static final String HTML5_REWRITE_FILTER = "html5-rewrite-filter";

    @Override
    public void init(FilterConfig config) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getAttribute(HTML5_REWRITE_FILTER) == null) {
            request.setAttribute(HTML5_REWRITE_FILTER, true);
            request.getRequestDispatcher("/").forward(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // nothing to do
    }

}

/*
 * Copyright © 2013-2018, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.w20.internal;

import com.google.common.base.Strings;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class Html5RewriteFilter implements Filter {
    private static final String HTML5_REWRITE_FILTER = "html5-rewrite-filter";
    private static final String CONNECTION = "Connection";
    private static final String UPGRADE = "Upgrade";
    private static final String WEBSOCKET = "websocket";
    private final String restPath;

    public Html5RewriteFilter(String restPath) {
        this.restPath = restPath;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (isAlreadyRedirected(request) || isWebSocketUpgrade(request)) {
            // Ignore already redirected or WebSocket upgrade requests
            filterChain.doFilter(request, response);
        } else {
            String path = ((HttpServletRequest) request).getRequestURI()
                    .substring(((HttpServletRequest) request).getContextPath().length());
            // Ignore REST requests and requests having a file extension
            if (isRestRequest(path) || hasFileExtension(path)) {
                filterChain.doFilter(request, response);
            } else {
                markRequestAsRedirected(request);
                request.getRequestDispatcher("/").forward(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        // nothing to do
    }

    private boolean isWebSocketUpgrade(ServletRequest request) {
        return request instanceof HttpServletRequest &&
                UPGRADE.equals(((HttpServletRequest) request).getHeader(CONNECTION)) &&
                WEBSOCKET.equals(((HttpServletRequest) request).getHeader(UPGRADE));
    }

    private boolean isAlreadyRedirected(ServletRequest request) {
        return request.getAttribute(HTML5_REWRITE_FILTER) != null;
    }

    private void markRequestAsRedirected(ServletRequest request) {
        request.setAttribute(HTML5_REWRITE_FILTER, true);
    }

    private boolean hasFileExtension(String path) {
        return path.lastIndexOf('.') > path.lastIndexOf('/');
    }

    private boolean isRestRequest(String path) {
        return !Strings.isNullOrEmpty(path) && !Strings.isNullOrEmpty(restPath) && path.startsWith(restPath);
    }
}

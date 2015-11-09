/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.w20.internal;


import com.google.inject.Inject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class MasterpageServlet extends HttpServlet {
    @Inject
    private MasterPageBuilder masterPageBuilder;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getRequestURI().endsWith("/")) {
            String queryString = req.getQueryString();

            if (queryString != null) {
                resp.sendRedirect(req.getRequestURI() + "/" + queryString);
            } else {
                resp.sendRedirect(req.getRequestURI() + "/");
            }
        } else {
            String result = masterPageBuilder.build(req);
            resp.setContentLength(result.length());
            resp.setContentType("text/html");
            resp.getWriter().write(result);
        }
    }
}

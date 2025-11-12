package com.biit.webforms.rest.security;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.webservice.rest.RestAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class WebformsRestAuthenticationFilter extends RestAuthenticationFilter {

    private WebformsRestAuthorizationService webformsRestAuthorizationService;

    /**
     * The extended parent needs to know what authorization service must call, so this method passes the specific
     * authorization class needed by the parent to filter the request.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException,
            ServletException {
        if (request instanceof HttpServletRequest) {
            request.setAttribute(AUTHORIZATION_INSTANCE, webformsRestAuthorizationService);
            super.doFilter(request, response, filter);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        webformsRestAuthorizationService = WebApplicationContextUtils.getRequiredWebApplicationContext(
                filterConfig.getServletContext()).getBean(WebformsRestAuthorizationService.class);
    }
}

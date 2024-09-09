package com.biit.webforms.rest.security;

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

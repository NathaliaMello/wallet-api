package com.nathaliamello.wallet.api.token;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {

    private static final String OAUTH_URL = "/oauth/token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String GRANT_TYPE = "grant_type";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (OAUTH_URL.equalsIgnoreCase(req.getRequestURI())
                && REFRESH_TOKEN.equals(req.getParameter(GRANT_TYPE))
                && req.getCookies() != null) {

            String refreshToken =
                    Stream.of(req.getCookies())
                            .filter(cookie -> "refreshToken".equals(cookie.getName()))
                            .findFirst()
                            .map(Cookie::getValue)
                            .orElse(null);

            req = new MyServletRequestWrapper(req, refreshToken);
        }

        chain.doFilter(req, response);

    }

    static class MyServletRequestWrapper extends HttpServletRequestWrapper {

        private String refreshToken;

        public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put(REFRESH_TOKEN, new String[] { refreshToken });
            map.setLocked(true);
            return map;
        }

    }

}

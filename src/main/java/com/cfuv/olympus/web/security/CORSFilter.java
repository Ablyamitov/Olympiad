package com.cfuv.olympus.web.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//public class CORSFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request = (HttpServletRequest) req;
//        response.setHeader("Access-Control-Allow-Headers", "Date, Content-Type, Accept, X-Requested-With, Authorization, From, X-Auth-Token, Request-Id, ngrok-skip-browser-warning");
//        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
////        String origin = request.getHeader("Origin");
////        List<String> allowedOrigins = Arrays.asList("http://localhost:3000", "https://siqalexx.github.io/");
////
////        if (allowedOrigins.contains(origin)) {
////            response.setHeader("Access-Control-Allow-Origin", origin);
////        }
//        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000, https://siqalexx.github.io/");
//        response.setHeader("Access-Control-Allow-Origin", "*");
//
//
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//
//
//        chain.doFilter(req, res);
//    }
//
//}

public class CORSFilter implements Filter {
    private final List<String> allowedOrigins = Arrays.asList(
            "http://localhost:3000",
            "https://siqalexx.github.io",
            "https://olympus.ydns.eu"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String origin = request.getHeader("Origin");

        // Проверка, разрешен ли origin
        if (origin != null && allowedOrigins.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        response.setHeader("Access-Control-Allow-Headers", "Date, Content-Type, Accept, X-Requested-With, Authorization, From, X-Auth-Token, Request-Id, ngrok-skip-browser-warning");
        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");

        // Обработка preflight-запросов (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

}

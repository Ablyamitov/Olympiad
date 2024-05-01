package com.example.olympiad.web.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Headers", "Date, Content-Type, Accept, X-Requested-With, Authorization, From, X-Auth-Token, Request-Id");
        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
        response.setHeader("Access-Control-Allow-Credentials", "true");
//        String origin = request.getHeader("Origin");
//        List<String> allowedOrigins = Arrays.asList("http://localhost:3000", "https://siqalexx.github.io/");
//
//        if (allowedOrigins.contains(origin)) {
//            response.setHeader("Access-Control-Allow-Origin", origin);
//        }
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000, https://siqalexx.github.io/");
        response.setHeader("Access-Control-Allow-Origin", "*");


        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");


        chain.doFilter(req, res);
    }

}

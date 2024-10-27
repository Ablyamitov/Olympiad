package com.cfuv.olympus.web.security;

import com.cfuv.olympus.domain.exception.entity.user.UserNotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        if (bearerToken!=null && bearerToken.startsWith("Bearer ")){
            bearerToken = bearerToken.substring(7);
        }
        if (bearerToken!=null && !bearerToken.equals("null") && jwtTokenProvider.validateToken(bearerToken)){
            try{
                Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                if(authentication!=null){
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (UserNotFoundException ignored){}

        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}



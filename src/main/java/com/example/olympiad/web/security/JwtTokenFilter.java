package com.example.olympiad.web.security;

import com.example.olympiad.domain.exception.entity.UserNotFoundException;
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
            try{    //если токен валидный, получаем аутентификацию
                Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                if(authentication!=null){   //пользователь аутентифицирован
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (UserNotFoundException ignored){}

        }
        filterChain.doFilter(servletRequest,servletResponse);

        /*Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
        String accessToken = null;
        if (cookies!=null){

            accessToken = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("access"))
                    .findFirst()
                    .map(Cookie::getValue).orElse(null);
        }
        if (cookies!=null && jwtTokenProvider.validateToken(accessToken)){
            try{    //если токен валидный, получаем аутентификацию
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                if(authentication!=null){   //пользователь аутентифицирован
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (ResourceNotFoundException ignored){}

        }
        filterChain.doFilter(servletRequest,servletResponse);*/



    }
}

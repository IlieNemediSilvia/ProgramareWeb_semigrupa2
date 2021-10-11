package com.example.laborator1;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@WebFilter("/students")
public class AuthenticationFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if("GET".equalsIgnoreCase(request.getMethod()) || "HEAD".equalsIgnoreCase(request.getMethod())){
            chain.doFilter(request, response);
        }
        else {
            String authorization = request.getHeader("Authorization");
            if(authorization == null || authorization.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            else if(!authorization.startsWith("Basic ")){
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            } else {
               String[] segments = new String(Base64.getDecoder().decode(authorization.substring("Basic ".length()))).split(":");
               if(segments.length == 2 && "demo".equals(segments[0]) && "demoPass".equals(segments[1])){
                   chain.doFilter(request, response);
               } else {
                   response.setStatus(HttpServletResponse.SC_FORBIDDEN);
               }
            }
        }
    }
}

package com.rohit.expensetracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(  HttpServletRequest request,
                 HttpServletResponse response,
                 FilterChain filterChain) throws ServletException, IOException
    {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
        {
            filterChain.doFilter( request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        String username;  // = jwtService.extractUsername(token);

        // Authentication will be established here // in the next step.
        try {
            username = jwtService.extractUsername(token);
        } catch (Exception exception) {

            filterChain.doFilter( request, response);
            return;
        }

        if (username != null
                && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                Authentication authentication =
                        UsernamePasswordAuthenticationToken.authenticated(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
           }
        }
         filterChain.doFilter(request, response);
    }
}

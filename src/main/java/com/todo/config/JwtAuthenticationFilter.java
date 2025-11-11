package com.todo.config;

import com.todo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // It checks every request happening
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    //Lazy annotation makes sure that user details is being used when it is requested
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, @Lazy UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // This method runs in every request happened
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Checking authHeader is valid or not
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Okay, I’m done with my logic in this filter — now pass the request and response to the next filter in the chain
            // If the request does not contain a JWT token, we simply skip our JWT validation logic.
            filterChain.doFilter(request, response);
            return;
        }

        // Retrieving user info from header
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // If user email is extracted but the security context is not set, then authenticate the user, depends on the given info
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); // Retrieving user info from database
            // Check if token is valid, if it is, create auth token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // Setting details and authentication - building security context
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // If everything is successful, continue with the process
        // Okay, I’ve checked/validated JWT. Now continue processing normally.
        filterChain.doFilter(request, response);
    }
}

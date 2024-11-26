package com.Uber.User.Service.config;

import com.Uber.User.Service.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MyUserDetail userDetailsService;


    private static final Logger log= LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String requestURI = request.getRequestURI();
            if (requestURI.contains("/user/login") || requestURI.contains("/user/addUser")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 1. Extract JWT token from header
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            final String userEmail = jwtUtils.extractClaims(jwt).getSubject();

            // 2. Check if we have a user email and no existing authentication
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("Processing authentication for user: {}", userEmail);

                // 3. Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // 4. Validate the token
                if (jwtUtils.validateJwt(jwt,userDetails.getUsername())) {
                    log.debug("Valid JWT token for user: {}", userEmail);

                    // 5. Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,          // principal (user details)
                            null,
                            userDetails.getAuthorities()
                    );

                    // 6. Add request details to authentication token
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 7. Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Set authentication in security context for user: {}", userEmail);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }
}

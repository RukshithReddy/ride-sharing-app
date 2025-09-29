package com.rapo.rapo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
 // ADD THIS LIST of public paths at the top of the class
    private final List<AntPathRequestMatcher> publicEndpoints = Arrays.asList(
        new AntPathRequestMatcher("/api/auth/**"),
        new AntPathRequestMatcher("/api/locations/**"),
        new AntPathRequestMatcher("/api/fare-estimate"),
        new AntPathRequestMatcher("/api/rides/search", "GET")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	// --- START DEBUG LOGGING ---
        System.out.println("\n>>> [FILTER] Intercepting request for URL: " + request.getRequestURI());
        // --- END DEBUG LOGGING ---

    	
    	 // If the request is for a public endpoint, skip the filter entirely
        if (publicEndpoints.stream().anyMatch(matcher -> matcher.matches(request))) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwt = getJwtFromRequest(request);
            
            // --- START DEBUG LOGGING ---
            System.out.println(">>> [FILTER] JWT found in request header: " + jwt);
            // --- END DEBUG LOGGING ---

            if (StringUtils.hasText(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                
             // --- START DEBUG LOGGING ---
                System.out.println(">>> [FILTER] UserDetails loaded from DB: " + userDetails.getUsername() + " | Roles: " + userDetails.getAuthorities());
                // --- END DEBUG LOGGING ---
                
                if (tokenProvider.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    
                 // --- START DEBUG LOGGING ---
                    System.out.println(">>> [FILTER] SUCCESS: Security context set for user '" + username + "'");
                    // --- END DEBUG LOGGING ---
                    
                }
            }
        } catch (Exception ex) {
        	
        	// --- START DEBUG LOGGING ---
            System.out.println(">>> [FILTER] ERROR: An exception occurred: " + ex.getMessage());
            // --- END DEBUG LOGGING ---	
        	
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
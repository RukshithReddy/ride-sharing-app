package com.rapo.rapo.config;

import com.rapo.rapo.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // --- UPDATED: Integrated a more flexible CORS configuration ---
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
            		
            		 // Allow all CORS pre-flight OPTIONS requests
                   .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                    
                // Public endpoints
                .requestMatchers("/", "/index.html", "/favicon.ico", "/*.js", "/*.css").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                // --- UPDATED: Made location and fare-estimate endpoints public ---
                .requestMatchers("/api/locations/**", "/api/fare-estimate").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/rides/search").permitAll()

                // Driver endpoints
                .requestMatchers(HttpMethod.POST, "/api/rides").hasAuthority("ROLE_DRIVER")
                .requestMatchers(HttpMethod.GET, "/api/rides/my-rides").hasAuthority("ROLE_DRIVER")

                // Passenger endpoints
                .requestMatchers(HttpMethod.POST, "/api/bookings").hasAuthority("ROLE_PASSENGER")
                .requestMatchers(HttpMethod.GET, "/users/me/bookings").hasAuthority("ROLE_PASSENGER")
                .requestMatchers(HttpMethod.POST, "/api/payments/process").hasAuthority("ROLE_PASSENGER")

                // All other requests must be authenticated
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    // --- UPDATED: Replaced CorsFilter with a more modern CorsConfigurationSource bean ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // This allows requests from any origin, which is suitable for development.
        configuration.setAllowedOrigins(Arrays.asList("*")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this configuration to all paths in the application.
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
}
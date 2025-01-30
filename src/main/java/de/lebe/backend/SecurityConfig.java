package de.lebe.backend;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/customer/**","/customer/*", "/actuator/**", "/actuator/*").permitAll()  // Allow access without authentication
                    .requestMatchers("/advisor/**")
                    .authenticated()  // Require authentication for /advisor/**
                    .anyRequest().authenticated()  // All other requests need authentication
            )
            .oauth2Login(oauth2Login ->
                oauth2Login
                    .loginPage("/oauth2/authorization/azure-dev") 
            )
            .oauth2Client(Customizer.withDefaults());

        return http.build();
    }
	
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("*"));
	    configuration.setAllowedMethods(Arrays.asList("*"));
	    configuration.setAllowedHeaders(Arrays.asList("*"));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/actuator/**", configuration);
	    source.registerCorsConfiguration("/advisor/**", configuration);
	    
	    CorsConfiguration customer = new CorsConfiguration();
	    customer.setAllowedOrigins(Arrays.asList("http://localhost:4001", "https://lebe-solarenergie.de"
	    		, "https://lebesolarenergie.de"
	    		, "https://lebesolar.de",
	    		"https://*.app.github.dev"));
	    
	    
	    
	    customer.setAllowedMethods(Arrays.asList("*"));
	    customer.setAllowedHeaders(Arrays.asList("*"));
	    source.registerCorsConfiguration("/customer/**", configuration);
	    return source;
	}
}
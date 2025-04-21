package com.promotion.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;


import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

import static com.promotion.user.entity.Permission.*;
import static com.promotion.user.entity.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**")
                .permitAll()


                //Promotion Ticket Endpont
                .requestMatchers("/api/v1/promotion/tc/**").hasAnyRole(ADMIN.name(),AGENT.name(),USER.name())
                .requestMatchers(GET, "api/v1/promotion/tc/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(POST, "api/v1/promotion/tc/**").hasAuthority(String.valueOf(ADMIN_CREATE))
                .requestMatchers(GET, "api/v1/promotion/tc/ticketHistory").hasAuthority(String.valueOf(ADMIN_READ))

                .requestMatchers("/api/v1/promotion/tt/**").hasAnyRole(ADMIN.name(),AGENT.name(),USER.name())
                .requestMatchers(GET, "api/v1/promotion/tt/ticketTransfer").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(AGENT_READ))
                .requestMatchers(POST, "api/v1/promotion/tt/ticketTransfer").hasAuthority(String.valueOf(ADMIN_CREATE))

                .requestMatchers("/api/v1/promotion/g/**").hasAnyRole(ADMIN.name(),AGENT.name(),USER.name())
                .requestMatchers(GET, "api/v1/promotion/g/gameAll").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(POST, "api/v1/promotion/g/game").hasAuthority(String.valueOf(ADMIN_CREATE))
                .requestMatchers(PUT, "api/v1/promotion/g/game").hasAuthority(String.valueOf(ADMIN_UPDATE))
                .requestMatchers(POST, "api/v1/promotion/g/assignGame").hasAuthority(String.valueOf(ADMIN_CREATE))

                .requestMatchers("/api/v1/promotion/rc/**").hasAnyRole(ADMIN.name(),AGENT.name(),USER.name())
                .requestMatchers(GET, "api/v1/promotion/rc/role").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(POST, "api/v1/promotion/rc/role").hasAuthority(String.valueOf(ADMIN_CREATE))

                .requestMatchers("/api/v1/promotion/nvc/**").hasAnyRole(ADMIN.name(),AGENT.name(),USER.name())
                .requestMatchers(GET, "api/v1/promotion/nvc/navigation").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(POST, "api/v1/promotion/nvc/navigation").hasAuthority(String.valueOf(ADMIN_CREATE))

                .requestMatchers("/api/v1/promotion/uc/**").hasAnyRole(ADMIN.name(),AGENT.name(),USER.name())
                .requestMatchers(GET, "api/v1/promotion/uc/profile").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(GET, "api/v1/promotion/uc/getAgent").hasAuthority(String.valueOf(ADMIN_READ))

                .requestMatchers("/api/v1/promotion/gi/**").hasAnyRole(ADMIN.name(),AGENT.name(),USER.name())
                .requestMatchers(POST, "api/v1/promotion/gi/gameItem").hasAuthority(String.valueOf(AGENT_CREATE))
                .requestMatchers(GET, "api/v1/promotion/gi/gameItem/**").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(PUT, "api/v1/promotion/gi/gameItem").hasAnyAuthority(String.valueOf(ADMIN_UPDATE),String.valueOf(AGENT_UPDATE))

                .requestMatchers("/api/v1/promotion/gtc/**").hasAnyRole(ADMIN.name(),AGENT.name(),USER.name())
                .requestMatchers(POST, "api/v1/promotion/gtc/gameTicket").hasAuthority(String.valueOf(AGENT_CREATE))
                .requestMatchers(GET, "api/v1/promotion/gtc/gameTicket").hasAnyAuthority(String.valueOf(ADMIN_READ),String.valueOf(AGENT_READ),String.valueOf(USER_READ))
                .requestMatchers(PUT, "api/v1/promotion/gtc/gameTicket").hasAnyAuthority(String.valueOf(ADMIN_UPDATE),String.valueOf(AGENT_UPDATE))


                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter , UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

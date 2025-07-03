package com.pcfinal.apiapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    
    @Autowired
    CustomUserDetailsService userDetailsService;
    
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf
                // Deshabilitar CSRF solo para API REST
                .ignoringRequestMatchers("/api/**")
            )
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos de API
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                // Swagger UI y OpenAPI endpoints
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                // Endpoints web públicos
                .requestMatchers("/web/login", "/web/login/**", "/web/register", "/web/register/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                .requestMatchers("/").permitAll()
                // Endpoints protegidos para API - solo usuarios autenticados con JWT
                .requestMatchers("/api/productos/**").authenticated()
                // Endpoints web protegidos - requieren autenticación web
                .requestMatchers("/web/**").authenticated()
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            // Configuración para vistas web con form login
            .formLogin(form -> form
                .loginPage("/web/login")
                .loginProcessingUrl("/web/login")
                .defaultSuccessUrl("/web/dashboard", true)
                .failureUrl("/web/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/web/logout")
                .logoutSuccessUrl("/web/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Configuración para API REST (mantener JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        // Para API REST, devolver 403 JSON
                        response.setStatus(403);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Acceso denegado\"}");
                    } else {
                        // Para vistas web, redirigir al login
                        response.sendRedirect("/web/login");
                    }
                })
            );
        
        // Agregar JWT filter para todas las rutas (se aplicará solo donde sea necesario)
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        http.authenticationProvider(authenticationProvider());
        
        return http.build();
    }
}

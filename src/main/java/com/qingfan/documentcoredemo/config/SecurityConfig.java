package com.qingfan.documentcoredemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // ===== Roles (Spring Security auto add "ROLE_" pre for hasRole check) =====
    public static final String CREATOR = "CREATOR";
    public static final String REVIEWER = "REVIEWER";
    public static final String APPROVER = "APPROVER";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/documents", true)
                        .permitAll()
                )
                .logout(Customizer.withDefaults())

                // H2 console convenient for local test
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()) // H2 console iframe can open
                )

                // auth rule：static resource、login page、H2
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/error",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**",
                                "/h2-console/**"
                        ).permitAll()

                        // ========== Document Web Pages (Thymeleaf) ==========
                        .requestMatchers(HttpMethod.GET, "/documents/**")
                        .hasAnyRole(CREATOR, REVIEWER, APPROVER)

                        // create/edit Draft：Creator
                        .requestMatchers(HttpMethod.GET, "/documents/new").hasRole(CREATOR)
                        .requestMatchers(HttpMethod.POST, "/documents").hasRole(CREATOR)
                        .requestMatchers(HttpMethod.GET, "/documents/*/edit").hasRole(CREATOR)
                        .requestMatchers(HttpMethod.POST, "/documents/*/update").hasRole(CREATOR)

                        .requestMatchers(HttpMethod.POST, "/documents/*/submit").hasRole(CREATOR)
                        .requestMatchers(HttpMethod.POST, "/documents/*/review").hasRole(REVIEWER)
                        .requestMatchers(HttpMethod.POST, "/documents/*/request-changes").hasRole(REVIEWER)
                        .requestMatchers(HttpMethod.POST, "/documents/*/approve").hasRole(APPROVER)
                        .requestMatchers(HttpMethod.POST, "/documents/*/reject").hasRole(APPROVER)
                        .requestMatchers(HttpMethod.POST, "/documents/*/archive").hasRole(APPROVER)

                        // ========== Document REST APIs (Postman) ==========
                        // GET：
                        .requestMatchers(HttpMethod.GET, "/api/documents/**")
                        .hasAnyRole(CREATOR, REVIEWER, APPROVER)

                        // Draft CRUD：Creator
                        .requestMatchers(HttpMethod.POST, "/api/documents").hasRole(CREATOR)
                        .requestMatchers(HttpMethod.PUT, "/api/documents/*").hasRole(CREATOR)

                        // Lifecycle actions
                        .requestMatchers(HttpMethod.POST, "/api/documents/*/submit").hasRole(CREATOR)
                        .requestMatchers(HttpMethod.POST, "/api/documents/*/review").hasRole(REVIEWER)
                        .requestMatchers(HttpMethod.POST, "/api/documents/*/request-changes").hasRole(REVIEWER)
                        .requestMatchers(HttpMethod.POST, "/api/documents/*/approve").hasRole(APPROVER)
                        .requestMatchers(HttpMethod.POST, "/api/documents/*/reject").hasRole(APPROVER)
                        .requestMatchers(HttpMethod.POST, "/api/documents/*/archive").hasRole(APPROVER)

                        // audit log
                        .requestMatchers(HttpMethod.GET, "/api/documents/*/audit-logs")
                        .hasAnyRole(CREATOR, REVIEWER, APPROVER)

                        // other request：need to log in
                        .anyRequest().authenticated()
                );

        return http.build();
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails creator = User.withUsername("creator")
                .password(encoder.encode("pass123"))
                .roles(CREATOR)
                .build();

        UserDetails reviewer = User.withUsername("reviewer")
                .password(encoder.encode("pass123"))
                .roles(REVIEWER)
                .build();

        UserDetails approver = User.withUsername("approver")
                .password(encoder.encode("pass123"))
                .roles(APPROVER)
                .build();

        return new InMemoryUserDetailsManager(creator, reviewer, approver);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

package com.example.websocketchat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Slf4j
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers().frameOptions().sameOrigin()
                .and()
                .formLogin()
                .successHandler(
                        new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                Authentication authentication)
                                    throws IOException, ServletException {
                                log.info("[login success!!] redirect to /chat/room ... bye bye ^_^");
                                response.sendRedirect("/chat/room");
                            }
                        }
                )
                .and()
                .authorizeRequests()
                .antMatchers("/chat/**").hasRole("USER")
                .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = passwordEncoder();
        UserDetails irumi = User.builder()
                .username("irumi")
                .password(passwordEncoder.encode("irumi"))
                .roles("USER")
                .build();
        UserDetails customer = User.builder()
                .username("customer")
                .password(passwordEncoder.encode("customer"))
                .roles("USER")
                .build();
        UserDetails guest = User.builder()
                .username("guest")
                .password(passwordEncoder.encode("guest"))
                .roles("GUEST")
                .build();
        return new InMemoryUserDetailsManager(irumi, customer, guest);
    }

}

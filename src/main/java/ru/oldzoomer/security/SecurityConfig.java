package ru.oldzoomer.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;

@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService users() {
        return new InMemoryUserDetailsManager(
            new User("admin", "{noop}admin",
            List.of(new SimpleGrantedAuthority("ADMIN"))));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/frontend/**",
                                "/VAADIN/**",
                                "/*.ico",
                                "/*.png",
                                "/*.jpg",
                                "/*.css",
                                "/*.js",
                                "/error",
                                "/public/**"
                        ).permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}

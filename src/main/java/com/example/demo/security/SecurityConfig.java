package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Definizione del provider di autenticazione
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Encoder di password: usa testo in chiaro (solo per test!)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // SOLO PER TEST!
    }

    // Configurazione dei filtri di sicurezza (login, autorizzazioni, ecc.)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // DISABILITA CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/auth", "/test", "/register", "/login").permitAll()
                        .anyRequest().authenticated() // tutte le richieste richiedono login
                )
                .formLogin(form -> form
                        .loginPage("/auth") // opzionale: puoi usare la login predefinita rimuovendo questa riga
                        .loginProcessingUrl("/process-login")
                        .defaultSuccessUrl("/home", true) // 👈 redirect after login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth")
                        .permitAll()
                );
        return http.build();
    }
}

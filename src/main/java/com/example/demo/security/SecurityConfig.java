//Questo package contiene classi dedicate alla sicurezza dell'applicazione
package com.example.demo.security;

//Importo delle annotazioni e dei componenti di Spring Security
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//Annotazione che indica che la classe contiene delle configurazioni
@Configuration
public class SecurityConfig {

    //Servizio personalizzato per il recupero delle credenziali dell'utente e iniezione tramite costruttore
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Definizione del provider di autenticazione
    //Viene utilizzato il CustomUserDetailService per caricare gli utenti e l'encoder
    //scelto per validarne le password
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Encoder di password: usa testo in chiaro
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Configurazione dei filtri di sicurezza (login, autorizzazioni, ecc.)
    // Di seguito sono definiti:
    // - quali URL sono pubblici e quali richiedono login
    // - il comportamento del form di login
    // - la gestione del logout
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //Disabilitazione del controllo CSFR
                .csrf(csrf -> csrf.disable())  // DISABILITA CSRF
                //Definizione di percorsi liberi e su richiesta di autenticazione
                .authorizeHttpRequests(auth -> auth
                        //Risorse Accessibili a tutti
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        //Rotte pubbliche
                        .requestMatchers("/auth", "/signup", "/register", "/login", "/useRegistration", "/terms").permitAll()
                        //Questo indica che il resto è sotto autenticazione
                        .anyRequest().authenticated()
                )
                //Configurazione del form di login
                .formLogin(form -> form
                        .loginPage("/auth") // Rimuovendo questa riga si usa il form di default
                        .loginProcessingUrl("/process-login")
                        .defaultSuccessUrl("/home", true) // Dove vieni direzzionato dopo login
                        .permitAll()
                )
                //Gestione del Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth")
                        .permitAll()
                );
        //Passa la configurazione a Spring Security
        return http.build();
    }
}

//Questo package gestisce la parte di sicurezza del progetto
package com.example.demo.security;

import com.example.demo.model.Utente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


/**
 *
 * Questa classe permette l'adattamento tra l'entità Utente e Spring Security
 * integrando l’autenticazione con i dati salvati nel database.
 */
public class CustomUserDetails implements UserDetails {

    private final Utente utente;

    //Il costruttore che riceve l'entità Utente
    public CustomUserDetails(Utente utente) {
        this.utente = utente;
    }

    //Restituisce ruoli/autorizzazioni dell'utente
    //Al momento no ritorna nulla, in quanto ruoli/autorizzazioni non sono implementati
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return utente.getPassword();
    }

    public String getNome() {
        return utente.getNome();
    }

    @Override
    public String getUsername() {
        return utente.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

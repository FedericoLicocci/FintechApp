package com.example.demo.security;


import com.example.demo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


//Recupera i dati di un utente dal DB durante il processo di autenticazione
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    //Iniezione del repository con il costruttore
    @Autowired
    public CustomUserDetailsService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    //Metodo usato per caricare un utente in base allo Username fornito in fase di login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //System.out.println("Cercando utente con nome: " + username);
        return utenteRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
    }

}

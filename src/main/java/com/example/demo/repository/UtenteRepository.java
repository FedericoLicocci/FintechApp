//Package che gestisce i repository del progetto
package com.example.demo.repository;

import com.example.demo.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    //Ricerca l'utente tramite il suo Nome
    Optional<Utente> findByNome(String nome);
    //Ricerca l'utente tramite il suo Username
    Optional<Utente> findByUsername(String username);
}





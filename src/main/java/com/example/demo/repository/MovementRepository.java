package com.example.demo.repository;

import com.example.demo.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Integer> {

    // Trova gli ultimi 5 movimenti per username utente ordinati per data decrescente
    List<Movement> findTop5ByUtenteIdOrderByDateDesc(Integer userId);

}

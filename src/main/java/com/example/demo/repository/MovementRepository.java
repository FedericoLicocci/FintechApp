package com.example.demo.repository;

import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Integer> {

    // Find last 5 movements for a given user ordered by date descending
    List<Movement> findTop5ByUtenteOrderByDateDesc(Utente utente);
}

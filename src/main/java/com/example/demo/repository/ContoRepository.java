package com.example.demo.repository;

import com.example.demo.model.conti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContoRepository extends JpaRepository<conti, Integer> {
    // Puoi aggiungere metodi custom se ti servono
}

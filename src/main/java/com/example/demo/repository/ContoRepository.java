package com.example.demo.repository;

import com.example.demo.model.conti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContoRepository extends JpaRepository<conti, Integer> {

    @Query("SELECT c FROM conti c WHERE c.utente.id = :utenteId")
    Optional<conti> findByUtenteId(@Param("utenteId") Integer utenteId);

    Optional<conti> findByNumeroConto(String numeroConto); // nuovo metodo
}

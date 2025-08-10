package com.example.demo.repository;

import com.example.demo.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Integer> {

    // Trova gli ultimi 5 movimenti per username utente ordinati per data decrescente
    // Prende gli ultimi 5 movimenti dove sender.id = userId ordinati per data decrescente
    List<Movement> findTop5BySenderIdOrderByDateDesc(Integer userId);


    // Somma di tutti i movimenti ricevuti da un certo sender con amount positivo (entrate)
    @Query("SELECT COALESCE(SUM(m.amount), 0) FROM Movement m WHERE m.sender.id = :senderId AND m.amount > 0")
    BigDecimal sumPositiveAmountBySenderId(@Param("senderId") Integer senderId);

    // Somma di tutti i movimenti ricevuti da un certo sender con amount negativo (uscite)
    @Query("SELECT COALESCE(SUM(m.amount), 0) FROM Movement m WHERE m.sender.id = :senderId AND m.amount < 0")
    BigDecimal sumNegativeAmountBySenderId(@Param("senderId") Integer senderId);

}

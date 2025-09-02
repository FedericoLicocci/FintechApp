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
    // nuovo metodo corretto
    List<Movement> findTop5ByIbanSenderOrderByDateDesc(String ibanSender);


    @Query("SELECT SUM(m.amount) FROM Movement m WHERE m.ibanSender = :iban AND m.amount > 0")
    BigDecimal sumPositiveAmountByIbanSender(@Param("iban") String iban);

    @Query("SELECT SUM(m.amount) FROM Movement m WHERE m.ibanSender = :iban AND m.amount < 0")
    BigDecimal sumNegativeAmountByIbanSender(@Param("iban") String iban);


}

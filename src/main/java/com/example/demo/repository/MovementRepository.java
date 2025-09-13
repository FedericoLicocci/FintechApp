//Pacchetto che gestisce i repository del progetto
package com.example.demo.repository;

//Import della classe del progetto + librerie Spring
import com.example.demo.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//Import librerie Java
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


//Fornisce metodi di accesso ai dati relativi alle transazioni
@Repository
public interface MovementRepository extends JpaRepository<Movement, Integer> {

    /**
     * Recupera gli ultimi 5 movimenti per un determinato IBAN mittente,
     * ordinati per data di esecuzione decrescente (dal più recente al meno recente).
     **/
    List<Movement> findTop5ByIbanSenderOrderByExecutionDateDesc(String ibanSender);

    //Calcola la somma di tutti gli importi positivi per un determiato IBAN
    @Query("SELECT SUM(m.amount) FROM Movement m WHERE m.ibanSender = :iban AND m.amount > 0")
    BigDecimal sumPositiveAmountByIbanSender(@Param("iban") String iban);

    //Calcola la somma di tutti gli importi negativi per un determiato IBAN
    @Query("SELECT SUM(m.amount) FROM Movement m WHERE m.ibanSender = :iban AND m.amount < 0")
    BigDecimal sumNegativeAmountByIbanSender(@Param("iban") String iban);


    //Recupera i movimeti degli ultimi 30 giorni di un IBAN
    @Query("SELECT m FROM Movement m WHERE m.ibanSender = :ibanSender AND m.executionDate >= :fromDate ORDER BY m.executionDate DESC")
    List<Movement> findLast30DaysMovements(@Param("ibanSender") String ibanSender,
                                           @Param("fromDate") LocalDateTime startDate);

}

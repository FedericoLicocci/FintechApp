package com.example.demo.service;

//Import classi del progetto
import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import com.example.demo.repository.MovementRepository;

//Import librerie Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Import librerie Java
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class MovementService {

    private final MovementRepository movementRepository;

    @Autowired
    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    //Restituisce gli ultimi 5 movimenti dell'utente
    public List<Movement> getLast5ByIbanSenderOrderByDateDesc(String ibanSender) {
        return movementRepository.findTop5ByIbanSenderOrderByExecutionDateDesc(ibanSender);
    }


    // Salvataggio del movimento bancario
    public void saveMovement(String ibanSender, String ibanReceiver, BigDecimal amount, String causale) {
        Movement movement = new Movement();
        movement.setIbanSender(ibanSender);
        movement.setIbanReceiver(ibanReceiver);
        movement.setAmount(amount);
        //movement.setDate(LocalDateTime.now());
        movement.setCausale(causale);
        movement.setStatus(Movement.Status.completed);

        movementRepository.save(movement);
    }

    //Recupera i movimenti degli ultimi 30 giorni
    public List<Movement> getLast30DaysMovements(String ibanSender) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return movementRepository.findLast30DaysMovements(ibanSender, thirtyDaysAgo);
    }
}

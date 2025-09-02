package com.example.demo.service;

import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import com.example.demo.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovementService {

    private final MovementRepository movementRepository;

    @Autowired
    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    public List<Movement> getLast5ByIbanSenderOrderByDateDesc(String ibanSender) {
        return movementRepository.findTop5ByIbanSenderOrderByDateDesc(ibanSender);
    }


    // Salvataggio con IBAN invece che Utente
    public void saveMovement(String ibanSender, String ibanReceiver, BigDecimal amount, String causale) {
        Movement movement = new Movement();
        movement.setIbanSender(ibanSender);
        movement.setIbanReceiver(ibanReceiver);
        movement.setAmount(amount);
        movement.setDate(LocalDateTime.now());
        movement.setCausale(causale);
        movement.setStatus(Movement.Status.completed);

        movementRepository.save(movement);
    }
}

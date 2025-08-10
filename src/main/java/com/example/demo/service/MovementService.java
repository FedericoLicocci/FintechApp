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

    public List<Movement> getLast5MovementsBySenderId(Integer senderId) {
        return movementRepository.findTop5BySenderIdOrderByDateDesc(senderId);
    }

    public void saveMovement(Utente sender, Utente receiver, BigDecimal amount, Utente utente) {
        Movement movement = new Movement();
        movement.setSender(sender);
        movement.setReceiver(receiver);
        movement.setAmount(amount);
        movement.setDate(LocalDateTime.now());
        movement.setUtente(utente);
        movement.setStatus(Movement.Status.completed);

        movementRepository.save(movement);
    }
}

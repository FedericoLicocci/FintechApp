package com.example.demo.controller;

import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import com.example.demo.repository.MovementRepository;
import com.example.demo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MovementController {

    private final MovementRepository movementRepository;
    private final UtenteRepository utenteRepository;

    @Autowired
    public MovementController(MovementRepository movementRepository, UtenteRepository utenteRepository) {
        this.movementRepository = movementRepository;
        this.utenteRepository = utenteRepository;
    }

    @GetMapping("/movements")
    public List<Movement> getLastFiveMovements(Principal principal) {
        // Recupera utente dal nome username (da Principal)
        Optional<Utente> userOptional = utenteRepository.findByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return List.of(); // lista vuota se utente non trovato o si potrebbe gestire errore
        }
        Utente user = userOptional.get();

        return movementRepository.findTop5BySenderIdOrderByDateDesc(user.getId());
    }
}

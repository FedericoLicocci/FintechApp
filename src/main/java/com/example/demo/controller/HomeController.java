package com.example.demo.controller;

import com.example.demo.model.Utente;
import com.example.demo.model.Movement;
import com.example.demo.repository.UtenteRepository;
import com.example.demo.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private MovementRepository movementRepository;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {

        String username = principal.getName();

        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        List<Movement> lastPayments = movementRepository.findTop5ByUtenteIdOrderByDateDesc(utente.getId());

        // Somma movimenti positivi con lo stesso senderId (supponiamo senderId = utente.getId())
        BigDecimal totaleEntrate = movementRepository.sumPositiveAmountBySenderId(utente.getId());

        // Somma movimenti negativi con lo stesso senderId (supponiamo senderId = utente.getId())
        BigDecimal totaleUscite = movementRepository.sumNegativeAmountBySenderId(utente.getId());

        // se non ci sono movimenti, sum può essere null
        if (totaleEntrate == null) {
            totaleEntrate = BigDecimal.ZERO;
        }

        // se non ci sono movimenti, sum può essere null
        if (totaleUscite == null) {
            totaleUscite = BigDecimal.ZERO;
        }

        model.addAttribute("saldo", utente.getSaldo());
        model.addAttribute("lastPayments", lastPayments);
        model.addAttribute("entrate", totaleEntrate);
        model.addAttribute("uscite", totaleUscite);

        return "home";
    }
}

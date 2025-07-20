package com.example.demo.controller;

import com.example.demo.model.Utente;
import com.example.demo.repository.UtenteRepository;
import com.example.demo.service.MovementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class PaymentController {

    private final MovementService movementService;
    private final UtenteRepository utenteRepository;

    @Autowired
    public PaymentController(MovementService movementService, UtenteRepository utenteRepository) {
        this.movementService = movementService;
        this.utenteRepository = utenteRepository;
    }

    @GetMapping("/payment")
    public String showPaymentForm() {
        return "index";
    }

    @PostMapping("/make-payment")
    public String makePayment(@RequestParam String sender,
                              @RequestParam BigDecimal amount,
                              @RequestParam String receiver,
                              HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Optional<Utente> userOptional = utenteRepository.findByNome(username);
        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }

        Utente user = userOptional.get();

        movementService.saveMovement(sender, receiver, amount, user);

        return "redirect:/index.html";
    }
}

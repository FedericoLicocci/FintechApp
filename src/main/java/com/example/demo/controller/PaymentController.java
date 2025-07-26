package com.example.demo.controller;

import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import com.example.demo.repository.MovementRepository;
import com.example.demo.repository.UtenteRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.MovementService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class PaymentController {

    private final MovementService movementService;
    private final UtenteRepository utenteRepository;
    private final MovementRepository movementRepository; // ✅ aggiunto

    @Autowired
    public PaymentController(MovementService movementService,
                             UtenteRepository utenteRepository,
                             MovementRepository movementRepository) {
        this.movementService = movementService;
        this.utenteRepository = utenteRepository;
        this.movementRepository = movementRepository; // ✅ salvato
    }

    @GetMapping("/payment")
    public String paymentPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        model.addAttribute("utente", userDetails.getUsername());

        return "payment";
    }

    @PostMapping("/make-payment")
    public String makePayment(@RequestParam String sender,
                              @RequestParam BigDecimal amount,
                              @RequestParam String receiver,
                              HttpSession session,
                              Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        model.addAttribute("utente", userDetails.getUsername());

        if (username == null) {
            return "redirect:/login";
        }

        Optional<Utente> userOptional = utenteRepository.findByNome(username);
        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }

        Utente user = userOptional.get();
        movementService.saveMovement(sender, receiver, amount, user);

        return "redirect:/PaymentSucces.html";
    }

    @GetMapping("/lastmovements")
    public String showLastMovements(Model model) {
        System.out.println("Entro");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Utente utente = utenteRepository.findByNome(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // ✅ CHIAMATA CORRETTA usando l'istanza e non il nome della classe
        List<Movement> lastPayments = movementRepository.findTop5ByUtenteOrderByDateDesc(utente);

        System.out.println("Entro2: " + lastPayments);
        model.addAttribute("lastPayments", lastPayments);

        System.out.println(lastPayments);
        return "lastmovements.html";
    }
}

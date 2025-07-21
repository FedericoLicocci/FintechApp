package com.example.demo.controller;

import com.example.demo.model.Utente;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.repository.UtenteRepository;
import com.example.demo.service.MovementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;


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
    public String paymentPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // nome utente loggato

        model.addAttribute("username", username);

        System.out.println("Principal: " + auth.getPrincipal());
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String utente = userDetails.getUsername();  // <--- qui prendi l'oggetto Utente vero
        model.addAttribute("utente", utente); // puoi aggiungere l'intero utente al model

        return "payment"; // o il tuo template HTML
    }



    @PostMapping("/make-payment")
    public String makePayment(@RequestParam String sender,
                              @RequestParam BigDecimal amount,
                              @RequestParam String receiver,
                              HttpSession session,
                              Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // nome utente loggato
        model.addAttribute("username", username);

        System.out.println("Principal: " + auth.getPrincipal());
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String utente = userDetails.getUsername();  // <--- qui prendi l'oggetto Utente vero
        model.addAttribute("utente", utente); // puoi aggiungere l'intero utente al model

        //String username = (String) session.getAttribute("username");
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
}

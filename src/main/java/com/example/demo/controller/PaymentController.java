package com.example.demo.controller;

import com.example.demo.dto.BonificoRequest;
import com.example.demo.model.Movement;
import com.example.demo.model.Utente;
import com.example.demo.model.conti;
import com.example.demo.repository.MovementRepository;
import com.example.demo.repository.UtenteRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.MovementService;
import com.example.demo.service.ContoService;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.repository.ContoRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class PaymentController {

    private final MovementService movementService;
    private final UtenteRepository utenteRepository;
    private final MovementRepository movementRepository;

    @Autowired
    public PaymentController(MovementService movementService,
                             UtenteRepository utenteRepository,
                             MovementRepository movementRepository) {
        this.movementService = movementService;
        this.utenteRepository = utenteRepository;
        this.movementRepository = movementRepository;
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

    @Autowired
    private ContoRepository contiRepository;

    @PostMapping("/payment")
    @ResponseBody
    public ResponseEntity<String> Payment(
            @Valid @ModelAttribute("bonificoRequest") BonificoRequest request,
            BindingResult result
    ) {

        System.out.println("Sono in payment...");

        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Errore di validazione:\n");
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(errorMessage.toString().trim());
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // ✅ Recupero il conto dell’utente
        conti conto = contiRepository.findByUtenteId(utente.getId())
                .orElseThrow(() -> new RuntimeException("Conto non trovato"));

        BigDecimal saldoDisponibile = conto.getSaldoDisponibile();
        BigDecimal importo = BigDecimal.valueOf(request.getImporto());

        System.out.println("Il saldo è: " + saldoDisponibile + " e l'importo da scalare è di: " + importo);

        // ✅ Controllo saldo
        if (saldoDisponibile.compareTo(importo) < 0) {
            return ResponseEntity.badRequest().body("Saldo insufficiente!");
        }

        // ✅ Decremento saldo e salvo
        conto.setSaldoDisponibile(saldoDisponibile.subtract(importo));
        contiRepository.save(conto);
        System.out.println("Il nuovo saldo è di: " + conto.getSaldoDisponibile());

        return ResponseEntity.ok("Bonifico eseguito con successo!");
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

        if (username == null) {
            return "redirect:/login";
        }

        Optional<Utente> userOptional = utenteRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }

        Utente user = userOptional.get();

        // Trova Utente sender
        Optional<Utente> senderUserOpt = utenteRepository.findByUsername(sender);
        if (senderUserOpt.isEmpty()) {
            model.addAttribute("error", "Sender non trovato");
            return "payment"; // o pagina errore
        }

        // Trova Utente receiver
        Optional<Utente> receiverUserOpt = utenteRepository.findByUsername(receiver);
        if (receiverUserOpt.isEmpty()) {
            model.addAttribute("error", "Receiver non trovato");
            return "payment"; // o pagina errore
        }

        Utente senderUser = senderUserOpt.get();
        Utente receiverUser = receiverUserOpt.get();

        movementService.saveMovement(senderUser, receiverUser, amount, user);

        return "redirect:/PaymentSucces.html";
    }

    @GetMapping("/lastmovements")
    public String showLastMovements(Model model) {
        System.out.println("Entro");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        List<Movement> lastPayments = movementRepository.findTop5BySenderIdOrderByDateDesc(utente.getId());

        System.out.println("Entro2: " + lastPayments);
        model.addAttribute("lastPayments", lastPayments);

        System.out.println(lastPayments);
        return "lastmovements.html";
    }

    @GetMapping("/pagamenti")
    public String paytest(Model model) {
        System.out.println("Sono in paytest...");
        return "pagamenti";
    }

    @GetMapping("/bonifico/terzi")
    public String bonificoTerzi(Model model) {
        System.out.println("Sono in bonifico terzi...");
        return "bonifico-terzi";
    }

    @GetMapping("/auth")
    public String auth(Model model) {
        System.out.println("Sono in auth...");
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth";
    }
}

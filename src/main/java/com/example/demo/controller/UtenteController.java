package com.example.demo.controller;

import com.example.demo.model.Utente;
import com.example.demo.model.conti;  // attenzione a maiuscole, usa Conto (non conti)

import com.example.demo.repository.ContoRepository;
import com.example.demo.repository.UtenteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
public class UtenteController {

    private final UtenteRepository utenteRepository;
    private final ContoRepository contoRepository;  // aggiunto il repository di Conto

    @Autowired
    public UtenteController(UtenteRepository utenteRepository, ContoRepository contoRepository) {
        this.utenteRepository = utenteRepository;
        this.contoRepository = contoRepository;  // inizializzato tramite constructor injection
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth"; // this is your register.html
    }

    @PostMapping("/signup")
    public String redirectToForm() {
        System.out.println("Sono in SIGNUP...");
        return "useRegistration";
    }

    @GetMapping("/terms")
    public String showTermsPage() {
        return "terms";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result
    ) {
        System.out.println("Sono in /Register...");

        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Errore di validazione:\n");
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            System.out.println(errorMessage);
            return ResponseEntity
                    .badRequest()
                    .body(errorMessage.toString().trim());
        }

        if (utenteRepository.findByUsername(request.getUsername()).isPresent()) {
            System.out.println("C'è già un utente con questo nome!");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Nome utente già registrato");
        }

        // Creazione Utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(request.getNome());
        nuovoUtente.setCognome(request.getCognome());
        nuovoUtente.setDataNascita(request.getDataNascita());
        nuovoUtente.setCodiceFiscale(request.getCodiceFiscale());
        nuovoUtente.setEmail(request.getEmail());
        nuovoUtente.setTelefono(request.getTelefono());
        nuovoUtente.setUsername(request.getUsername());
        nuovoUtente.setPassword(request.getPassword()); // ⚠️ Da criptare

        utenteRepository.save(nuovoUtente);
        System.out.println("Utente " + nuovoUtente.getUsername() + " aggiunto al DB");

        // Creazione Conto associato
        conti conto = new conti();
        conto.setUtente(nuovoUtente);
        conto.setNumeroConto(generaNumeroContoUnico());  // metodo da implementare per generare numero conto unico
        conto.setTipoConto("corrente");
        conto.setSaldoContabile(BigDecimal.ZERO);
        conto.setSaldoDisponibile(BigDecimal.ZERO);
        conto.setDataApertura(LocalDateTime.now());

        contoRepository.save(conto);  // qui uso l'istanza, non la classe!
        System.out.println("Conto associato creato per utente " + nuovoUtente.getUsername());

        return ResponseEntity
                .ok("Registrazione avvenuta con successo con conto associato");
    }

    // Implementa qui un metodo per generare un numero conto unico
    private String generaNumeroContoUnico() {
        // esempio semplice: puoi fare una random string, o basarti su sequenza DB, etc.
        return "IT" + System.currentTimeMillis();
    }
}

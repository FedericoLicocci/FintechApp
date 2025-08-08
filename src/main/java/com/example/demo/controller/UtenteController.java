package com.example.demo.controller;

import com.example.demo.model.Utente;
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



@Controller
public class UtenteController {

    private final UtenteRepository utenteRepository;

    @Autowired
    public UtenteController(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
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

        if (utenteRepository.findByNome(request.getNome()).isPresent()) {
            System.out.println("C'è già un utente con questo nome!");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Nome utente già registrato");
        }

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

        return ResponseEntity
                .ok("Registrazione avvenuta con successo");
    }




}

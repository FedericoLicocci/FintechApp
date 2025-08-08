package com.example.demo.controller;

import com.example.demo.model.Utente;
import com.example.demo.repository.UtenteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            Model model
    ) {
        System.out.println("Sono in /Register...");
        // If validation fails, return to form
        if (result.hasErrors()) {
            System.out.println("C'è stato un errore...");
            result.getAllErrors().forEach(error -> {
                System.out.println("Errore: " + error.toString());
            });
            return "auth"; // Thymeleaf will show field errors
        }

        // Check if username already exists
        if (utenteRepository.findByNome(request.getNome()).isPresent()) {
            model.addAttribute("error", "Nome utente già registrato");
            System.out.println("C'è giù un utente con questo nome!");
            return "auth";
        }

        // Create new user
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(request.getNome());
        nuovoUtente.setCognome(request.getCognome());
        nuovoUtente.setDataNascita(request.getDataNascita());//
        nuovoUtente.setCodiceFiscale(request.getCodiceFiscale());
        nuovoUtente.setEmail(request.getEmail());
        nuovoUtente.setTelefono(request.getTelefono());
        nuovoUtente.setUsername(request.getUsername());
        nuovoUtente.setPassword(request.getPassword()); // ⚠️ Encrypt in production

        utenteRepository.save(nuovoUtente);
        System.out.println("Utente " + nuovoUtente.getUsername() + " aggiunto al DB");

        return "auth";
    }



}

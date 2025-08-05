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


    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            Model model
    ) {
        // If validation fails, return to form
        if (result.hasErrors()) {
            System.out.println("C'è stato un errore...");
            return "auth"; // Thymeleaf will show field errors
        }

        // Check if username already exists
        if (utenteRepository.findByNome(request.getNome()).isPresent()) {
            model.addAttribute("error", "Nome utente già registrato");
            return "auth";
        }

        // Create new user
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(request.getNome());
        nuovoUtente.setPassword(request.getPassword()); // ⚠️ Encrypt in production

        utenteRepository.save(nuovoUtente);

        return "redirect:/login?registered";
    }



}

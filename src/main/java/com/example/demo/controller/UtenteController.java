package com.example.demo.controller;

import com.example.demo.repository.UtenteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UtenteController {

    private final UtenteRepository utenteRepository;

    @Autowired
    public UtenteController(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    // Mostra la pagina di login
    @GetMapping("/login")
    public String showLogin() {
        return "login"; // login.html nella cartella templates o static
    }

    // Logout (cancella sessione)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

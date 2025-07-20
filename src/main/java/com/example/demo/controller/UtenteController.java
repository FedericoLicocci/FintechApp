package com.example.demo.controller;

import com.example.demo.model.Utente;
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

    // Gestisce la form di login
    @PostMapping("/login")
    public String doLogin(@RequestParam String nome,
                          @RequestParam String password,
                          HttpSession session) {

        Utente user = utenteRepository.findByNome(nome).orElse(null);

        if (user != null && user.getPassword().equals(password)) {
            // Login corretto, salvo username in sessione
            session.setAttribute("username", user.getNome());
            return "redirect:/payment"; // pagina di pagamento
        } else {
            // Login fallito, ritorna a login con errore (puoi gestirlo meglio)
            return "redirect:/login?error=true";
        }
    }

    // Logout (cancella sessione)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

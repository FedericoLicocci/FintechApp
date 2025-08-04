package com.example.demo.controller;

import com.example.demo.model.Utente;
import com.example.demo.repository.UtenteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UtenteController {

    private final UtenteRepository utenteRepository;

    @Autowired
    public UtenteController(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String nome,
            @RequestParam String password,
            Model model
    ) {
        // Controlla se esiste già un utente con lo stesso nome
        if (utenteRepository.findByNome(nome).isPresent()) {
            System.out.println("Sono qui");
            model.addAttribute("error", "Nome utente già registrato");
            return "register"; // Torna alla pagina di registrazione con errore
        }

        // Crea il nuovo utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(nome);
        nuovoUtente.setPassword(password); // ⚠️ Da criptare in produzione

        utenteRepository.save(nuovoUtente);

        return "redirect:/login?registered"; // O la pagina che preferisci
    }


}

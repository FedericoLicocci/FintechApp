//Package dedicato alla gestione dei controller
package com.example.demo.controller;

//Import delle classi del progetto
import com.example.demo.model.Utente;
import com.example.demo.model.conti;
import com.example.demo.repository.ContoRepository;
import com.example.demo.repository.UtenteRepository;

//Import libreri Spring
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

//Importo librerie Java
import java.math.BigDecimal;
import java.time.LocalDateTime;


//Questo controller si occupa della gestione dell'utente (Registrazione, Autenticazione, ecc...)
@Controller
public class UtenteController {

    //Repository per la persistenza di utenti e conti
    private final UtenteRepository utenteRepository;
    private final ContoRepository contoRepository;  // aggiunto il repository di Conto

    //Inizione delle dipendenze con costruttore
    @Autowired
    public UtenteController(UtenteRepository utenteRepository, ContoRepository contoRepository) {
        this.utenteRepository = utenteRepository;
        this.contoRepository = contoRepository;  // inizializzato tramite constructor injection
    }

    //Rotta che si rimanda alla pagina di registrazione dell'utente
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth";
    }

    // Reindirizza alla pagina useRegistration
    @PostMapping("/signup")
    public String redirectToForm() {
        System.out.println("Sono in SIGNUP...");
        return "useRegistration";
    }

    //Mostra la pagina dei termini e condizioni
    @GetMapping("/terms")
    public String showTermsPage() {
        return "terms";
    }

    //Rotta per la registrazione di un nuovo utente
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request, //I valori vengono presi dal DTO
            BindingResult result
    ) {
        //System.out.println("Sono in /Register...");

        //Effettua un controllo sui campi inseriti
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Errore di validazione:\n");
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            System.out.println(errorMessage);
            return ResponseEntity
                    .badRequest()
                    .body(errorMessage.toString().trim());
        }

        //Verifica se c'è già un utente con lo stesso nome inserito nel sistema
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

        //Salvataggio dell'utente nel DB
        utenteRepository.save(nuovoUtente);
        //System.out.println("Utente " + nuovoUtente.getUsername() + " aggiunto al DB");

        // Creazione Conto associato
        conti conto = new conti();
        conto.setUtente(nuovoUtente);
        conto.setNumeroConto(generaNumeroContoUnico());  // metodo da implementare per generare numero conto unico
        conto.setTipoConto("corrente");
        conto.setSaldoContabile(BigDecimal.ZERO);
        conto.setSaldoDisponibile(BigDecimal.ZERO);
        conto.setDataApertura(LocalDateTime.now());

        //Salvataggio del conto nel DB
        contoRepository.save(conto);  // qui uso l'istanza, non la classe!
        //System.out.println("Conto associato creato per utente " + nuovoUtente.getUsername());

        return ResponseEntity
                .ok("Registrazione avvenuta con successo con conto associato");
    }

    // Generazione di un IBAN unico per il conto
    private String generaNumeroContoUnico() {
        //Il prefisso IT + Tempo in millisecondi
        return "IT" + System.currentTimeMillis();
    }
}

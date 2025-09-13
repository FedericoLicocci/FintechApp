package com.example.demo.service;

//Import delle classi del progetto
import com.example.demo.model.conti;
import com.example.demo.model.Utente;
import com.example.demo.repository.ContoRepository;
import com.example.demo.repository.UtenteRepository;

//Import librerie Spring
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Import librerie Java
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ContoService {

    private final ContoRepository contoRepository;
    private final UtenteRepository utenteRepository;

    // Iniezione delle dipendenze tramite costruttore (best practice)
    public ContoService(ContoRepository contoRepository, UtenteRepository utenteRepository) {
        this.contoRepository = contoRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Crea un nuovo conto per un utente esistente.
     * @param utenteId L'ID dell'utente a cui associare il conto.
     * @param numeroConto Il numero del nuovo conto.
     * @param tipoConto Il tipo di conto (es. "corrente").
     * @param saldoIniziale Il saldo di partenza.
     * @return L'entità Conto salvata.
     */
    @Transactional
    public conti creaContoPerUtente(int utenteId, String numeroConto, String tipoConto, BigDecimal saldoIniziale) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con id: " + utenteId));

        //Definizione del conto
        conti conto = new conti();
        conto.setUtente(utente);
        conto.setNumeroConto(numeroConto);
        conto.setTipoConto(tipoConto != null ? tipoConto : "corrente");
        conto.setSaldoContabile(saldoIniziale != null ? saldoIniziale : BigDecimal.ZERO);
        conto.setSaldoDisponibile(saldoIniziale != null ? saldoIniziale : BigDecimal.ZERO);

        //Salvataggio del conto
        return contoRepository.save(conto);
    }

    /**
     * Recupera il saldo disponibile del conto associato all'utente attualmente loggato.
     * Questo metodo è specifico per lo scenario "un solo conto per utente".
     *
     * @return Il valore del saldo disponibile, o BigDecimal.ZERO se l'utente o il conto non vengono trovati.
     */
    public BigDecimal getSaldoDisponibileUtenteCorrente() {
        // 1. Ottieni il Principal (che contiene i dettagli dell'utente) dal contesto di sicurezza
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Controlla che il Principal sia del tipo atteso (UserDetails)
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();

            // 2. Trova l'utente nel database usando lo username
            Optional<Utente> utenteOpt = utenteRepository.findByUsername(username);

            if (utenteOpt.isPresent()) {
                Integer utenteId = utenteOpt.get().getId();

                // 3. Recupero il conto associato all'ID
                Optional<conti> contoOpt = contoRepository.findByUtenteId(utenteId);

                // 4. Se il conto esiste, restituisci il suo saldo disponibile
                if (contoOpt.isPresent()) {
                    return contoOpt.get().getSaldoDisponibile();
                }
            }
        }

        // 5. In tutti gli altri casi (utente non trovato, conto non trovato), restituisci un valore sicuro
        return BigDecimal.ZERO;
    }
}
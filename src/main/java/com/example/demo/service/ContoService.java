package com.example.demo.service;

import com.example.demo.model.conti;
import com.example.demo.model.Utente;
import com.example.demo.repository.ContoRepository;
import com.example.demo.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ContoService {

    private final ContoRepository contoRepository;
    private final UtenteRepository utenteRepository;

    public ContoService(ContoRepository contoRepository, UtenteRepository utenteRepository) {
        this.contoRepository = contoRepository;
        this.utenteRepository = utenteRepository;
    }

    public conti creaContoPerUtente(int utenteId, String numeroConto, String tipoConto, BigDecimal saldoIniziale) {
        Optional<Utente> utenteOpt = utenteRepository.findById(utenteId);
        if (utenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato con id: " + utenteId);
        }

        Utente utente = utenteOpt.get();

        conti conto = new conti();
        conto.setUtente(utente);
        conto.setNumeroConto(numeroConto);
        conto.setTipoConto(tipoConto != null ? tipoConto : "corrente");
        conto.setSaldoContabile(saldoIniziale != null ? saldoIniziale : BigDecimal.ZERO);
        conto.setSaldoDisponibile(saldoIniziale != null ? saldoIniziale : BigDecimal.ZERO);
        conto.setDataApertura(LocalDateTime.now());

        return contoRepository.save(conto);
    }
}

package com.example.demo.model;

//Import delle librerie Java e Spring
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entità JPA che rappresenta un conto bancario associato a un utente.
 * Questa classe mappa la tabella "conti" del database.
 */

@Entity
@Table(name = "conti")
public class conti {

    //Chiave primaria auto incrementata
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Relazione N:1 → ogni conto appartiene a un singolo utente.
     * FetchType.LAZY: i dati dell’utente vengono caricati solo se richiesti, per ottimizzazione.
     * JoinColumn: specifica il vincolo di foreign key nella tabella "conti".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    //La colonna numero_conto deve contenere valori univoci, non nulli e di massimo 30 caratteri.
    @Column(name = "numero_conto", nullable = false, unique = true, length = 30)
    private String numeroConto;

    /**
     * Tipo di conto (default = "corrente").
     * Al momento è disponibile solo il conto corrente, ma si prevede un estensione
     */
    @Column(name = "tipo_conto", length = 50)
    private String tipoConto = "corrente";

    /**
     * Saldo contabile del conto (es. saldo effettivo registrato).
     * precision = 15 → numero totale di cifre, scale = 2 → due cifre decimali.
     * Default: 0.00
     */
    @Column(name = "saldo_contabile", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoContabile = BigDecimal.ZERO;

    /**
     * Saldo disponibile (quello che l’utente può realmente spendere).
     * Anch’esso inizializzato a 0.00.
     */
    @Column(name = "saldo_disponibile", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoDisponibile = BigDecimal.ZERO;

    //Data apertura del conto
    @Column(name = "data_apertura", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataApertura;


    public conti() {}

    // getter e setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public String getNumeroConto() {
        return numeroConto;
    }

    public void setNumeroConto(String numeroConto) {
        this.numeroConto = numeroConto;
    }

    public String getTipoConto() {
        return tipoConto;
    }

    public void setTipoConto(String tipoConto) {
        this.tipoConto = tipoConto;
    }

    public BigDecimal getSaldoContabile() {
        return saldoContabile;
    }

    public void setSaldoContabile(BigDecimal saldoContabile) {
        this.saldoContabile = saldoContabile;
    }

    public BigDecimal getSaldoDisponibile() {
        return saldoDisponibile;
    }

    public void setSaldoDisponibile(BigDecimal saldoDisponibile) {
        this.saldoDisponibile = saldoDisponibile;
    }

    public LocalDateTime getDataApertura() {
        return dataApertura;
    }

    public void setDataApertura(LocalDateTime dataApertura) {
        this.dataApertura = dataApertura;
    }
}

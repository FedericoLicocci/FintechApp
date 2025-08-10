package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conti")
public class conti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @Column(name = "numero_conto", nullable = false, unique = true, length = 30)
    private String numeroConto;

    @Column(name = "tipo_conto", length = 50)
    private String tipoConto = "corrente";

    @Column(name = "saldo_contabile", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoContabile = BigDecimal.ZERO;

    @Column(name = "saldo_disponibile", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoDisponibile = BigDecimal.ZERO;

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

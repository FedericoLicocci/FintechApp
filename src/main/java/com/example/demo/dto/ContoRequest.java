package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ContoRequest {

    @NotNull(message = "Id utente obbligatorio")
    private Integer utenteId;

    @NotBlank(message = "Numero conto obbligatorio")
    private String numeroConto;

    private String tipoConto = "corrente";  // default

    private BigDecimal saldoIniziale = BigDecimal.ZERO;

    // getter e setter
    public Integer getUtenteId() {
        return utenteId;
    }
    public void setUtenteId(Integer utenteId) {
        this.utenteId = utenteId;
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
    public BigDecimal getSaldoIniziale() {
        return saldoIniziale;
    }
    public void setSaldoIniziale(BigDecimal saldoIniziale) {
        this.saldoIniziale = saldoIniziale;
    }
}

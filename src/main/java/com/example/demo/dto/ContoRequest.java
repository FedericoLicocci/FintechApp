//Package per la validazione delle variabili
package com.example.demo.dto;

//Import librerie Spring
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//Import librerie Java
import java.math.BigDecimal;

//Dichiarazione classe pubblica
public class ContoRequest {

    @NotNull(message = "Id utente obbligatorio")
    private Integer utenteId;

    @NotBlank(message = "Numero conto obbligatorio")
    private String numeroConto;

    private String tipoConto = "corrente";  // default

    private BigDecimal saldoIniziale = BigDecimal.ZERO; //Inizializza il conto con €0

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

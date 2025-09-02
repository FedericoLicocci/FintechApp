package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDate;

@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String codiceFiscale;
    private String email;
    private String telefono;
    private String username;
    private String password;
    //private int saldo;

    // Lista di conti dell'utente
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<conti> conti;

    // Se vuoi mantenere anche movements, li lasci così (opzionale)
//    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Movement> movements;

    public Utente() {}

    // getter e setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public int getSaldo() {
//        return saldo;
//    }

//    public void setSaldo(int saldo) {
//        this.saldo = saldo;
//    }

    public List<conti> getConti() {
        return conti;
    }

    public void setConti(List<conti> conti) {
        this.conti = conti;
    }

//    public List<Movement> getMovements() {
//        return movements;
//    }

//    public void setMovements(List<Movement> movements) {
//        this.movements = movements;
//    }
}

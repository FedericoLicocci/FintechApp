//Package dedicato alla validazione
package com.example.demo.dto;

//Import della libreria per evitare campi vuoti
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Nome obbligatorio")
    private String nome;

    @NotBlank(message = "Password obbligatoria")
    private String password;

    // getter e setter

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

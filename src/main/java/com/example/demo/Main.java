package com.example.demo;

//Import librerie Spring
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;


@SpringBootApplication /* abilita la configurazione automatica di Spring Boot,
                         indica che questa classe è il punto di avvio dell'applicazione. */
@EnableJpaRepositories(basePackages = "com.example.demo.repository") //Specifica dove cercare i repository
@EntityScan(basePackages = "com.example.demo.model") //Specifica dove cercare le entità
@Controller  // Segnala che può gestire richieste HTTP

//Avvio dell'applicazione
public class Main {
    public static void main(String[] args) {
        try {
            SpringApplication.run(Main.class, args);
        }
        //stampare eventuali errori sulla console.
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EntityScan(basePackages = "com.example.demo.model")
@Controller  // Controller + Rest API
public class Main {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Main.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @GetMapping("/")
    public String Index() {
        return "index";
    }

    @GetMapping("/hello/US")
    public String hello() {
        return "Hello, World!";
    }


    @GetMapping("/error")
    public String error() {

        return "Errore, impossibile caricare la pagina!";

    }
}
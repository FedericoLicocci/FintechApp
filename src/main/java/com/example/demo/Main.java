package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController  // Controller + Rest API
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/")
    public String Home() {
        return "Letsgoski, sei nella Home";
    }

    @GetMapping("/hello/US")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/hello/IT")
    public String ciao() {

        int costo = 20000;
        String name = "Federico";

        return "Ciao " + name + " il tuo conto è di €" + costo + ", mondo!";

    }

    @GetMapping("/pagamenti/bonifico")
    public String bonifico(@RequestParam int Bonifico_uscita) {

        int conto_Fede = 20000;
        String name = "Federico";

        if( Bonifico_uscita > conto_Fede)
        {
            return name + " Non hai abbastanza soldi sul conto per eseguire questo bonifico!";
        }
        else
        {
            conto_Fede -= Bonifico_uscita;
            return "Bonifico di €" + Bonifico_uscita + " eseguito<br>Ora il tuo conto è di €" + conto_Fede;
        }
    }
}
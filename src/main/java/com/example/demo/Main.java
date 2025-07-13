package com.example.demo;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public String bonifico() {

        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connessione riuscita!");

            String query = "SELECT * FROM Utenti";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String nome = rs.getString("Nome");
                int saldo = rs.getInt("Saldo");
                System.out.println("ID: " + id + ", Nome: " + nome + ", Saldo: " + saldo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "Success!";

//        int conto_Fede = 20000000;
//        String name = "Federico";
//
//        if( Bonifico_uscita > conto_Fede)
//        {
//            return name + " Non hai abbastanza soldi sul conto per eseguire questo bonifico!";
//        }
//        else
//        {
//            conto_Fede -= Bonifico_uscita;
//            return "Bonifico di €" + Bonifico_uscita + " eseguito<br>Ora il tuo conto è di €" + conto_Fede;
//        }
    }


}
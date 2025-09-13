//Pacchetto che gestisce i repository del progetto
package com.example.demo.repository;

//Import delle classi del progetto + librerie Spring e Java
import com.example.demo.model.conti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ContoRepository extends JpaRepository<conti, Integer> {

    //Estra tutti i conti di un utente dalla tabella a partire dal suo ID utente
    @Query("SELECT c FROM conti c WHERE c.utente.id = :utenteId")
    Optional<conti> findByUtenteId(@Param("utenteId") Integer utenteId);

    //Recupera un conto in base al suo numero, query generato automaticamente da JPA
    Optional<conti> findByNumeroConto(String numeroConto);
}

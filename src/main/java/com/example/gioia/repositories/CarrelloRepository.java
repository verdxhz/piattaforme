package com.example.gioia.repositories;


import com.example.gioia.entity.Carrello;

import com.example.gioia.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Integer> {
    //ricerca semplice
    public Carrello findById(int id);
    @Query("select c.carrello from Cliente c where c.id_cliente=?1")
    public Carrello findByclient(int client);

    //verifica esistenza
    boolean existsById(int id);
}











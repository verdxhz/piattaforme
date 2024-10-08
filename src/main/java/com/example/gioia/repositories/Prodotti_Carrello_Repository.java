package com.example.gioia.repositories;



import com.example.gioia.entity.Carrello;
import com.example.gioia.entity.Prodotti_Carrello;
import com.example.gioia.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Prodotti_Carrello_Repository extends JpaRepository<Prodotti_Carrello, Integer> {

    //ricerca complessa
    Prodotti_Carrello findByCarrelloAndProdotto(Carrello carrello, Prodotto prodotto);
}

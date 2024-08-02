package com.example.gioia.repositories;


import com.example.gioia.entity.Ordine;

import com.example.gioia.entity.Cliente;
import com.example.gioia.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Integer> {
    //ricerca semplice
    public Ordine findById(int id);
    @Query("select o from Ordine o where o.carrello.cliente.id_cliente=?1")
    public List<Ordine> findByclient(int client);

    //ricerca più complessa
    @Query("select o from Ordine o where o.data>=?1 and o.data<= ?2")
    public List<Ordine> findByIntervalloTempo(int client, LocalDateTime inizio, LocalDateTime fine);
    @Query("select o from Ordine o where o.carrello in  (select p.carrello from Prodotti_Carrello p where p.prodotto= ?1)  ")
    public List<Ordine> findByProdotto(Prodotto prodotto);
    @Query("select o from Ordine o where lower(o.indirizzo) like lower(concat('%', ?1, '%'))")
    public List<Ordine> findByCitta(String città);
    //verifica esistenza
    boolean existsByCliente(Cliente cliente);


}











package com.example.gioia.repositories;

import com.example.gioia.entity.Prodotto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Integer> {
    //ricerca semplice
    @Query("select p from Prodotto p where p.categoria like lower(concat( '%', ?1, '%'))")
    public Page<Prodotto> findByCategoria(String categoria, Pageable pageable);
    public List<Prodotto> findByDisponibilità(int disp);

    //ricerca più complessa

    @Query("select p from Prodotto p where p.prezzo>=?1 and p.prezzo<=?2 ")
    public Page<Prodotto> findByIntervalloPrezzo(int prezzomin, int prezzomax, Pageable pageable);
    @Query("select p from Prodotto p where p.nome like lower(concat('%', ?1, '%')) or p.descrizione like lower(concat('%', ?1, '%'))")
    public Page<Prodotto> findByParole(String parola, Pageable pageable);
}

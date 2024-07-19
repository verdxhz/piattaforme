package com.example.gioia.repositories;

import com.example.gioia.entity.Prodotto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Integer> {
   //ricerca semplice
   // public Prodotto findById(int id);
    public Page<Prodotto> findByCategoria(String categoria, Pageable pageable);

    //ricerca più complessa
    @Query("SELECT p FROM Prodotto p WHERE lower(p.nome) LIKE lower(concat('%', :nome, '%')) ORDER BY p.nome ASC")
    Page<Prodotto> findByNameContainingIgnoreCaseOrderByNameAsc(@Param("nome") String name, Pageable pageable);
    @Query("select p from Prodotto p where p.prezzo>=?1 and p.prezzo<=?2 ")
    public Page<Prodotto> findByIntervalloPrezzo(int prezzomin, int prezzomax, Pageable pageable);
}

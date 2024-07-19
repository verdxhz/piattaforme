package com.example.gioia.repositories;

import com.example.gioia.entity.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    //ricerca semplice
    public Cliente findByEmail(String email);
    public Cliente findByUsername(String username);
    //ricerca più complessa
    @Query("SELECT u FROM Cliente u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) ORDER BY u.username")
    List<Cliente> findByUsernameContainingOrderByUsernameIgnoreCase(String username);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    //verifica esistenza
    public boolean existsByEmail(String email);
    public boolean existsByUsernameIgnoreCase(String username);
}
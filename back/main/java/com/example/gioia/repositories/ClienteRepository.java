package com.example.gioia.repositories;

import com.example.gioia.entity.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {


    //ricerca più complessa
    List<Cliente> findByNomeContainingIgnoreCase(String nome);



}
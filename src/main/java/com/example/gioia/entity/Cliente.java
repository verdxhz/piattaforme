package com.example.gioia.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente", nullable = false)
    private int id_cliente;

    @Basic
    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy ="cliente", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<Ordine> ordini ;

    @OneToOne
    @JsonIgnore
    private Carrello carrello;


}

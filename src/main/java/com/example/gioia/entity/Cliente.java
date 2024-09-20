package com.example.gioia.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;


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

    @OneToMany(mappedBy ="cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Ordine> ordini ;

    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private Carrello carrello;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(nome, cliente.nome) && Objects.equals(id_cliente, cliente.id_cliente);
    }

}

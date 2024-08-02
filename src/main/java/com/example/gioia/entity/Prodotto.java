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
@Table(name = "Prodotto")
public class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "nome", nullable = false)
    private String nome;

    @Basic
    @Column(name = "descrizione", nullable = true)
    private String descrizione;

    @Basic
    @Column(name = "prezzo", nullable = false)
    private float prezzo;

    @Basic
    @Column(name = "categoria", nullable = true)
    private String categoria;

    @Basic
    @Column(name = "disponibilità", nullable = false)
    private int disponibilità;

    @Basic
    @Column(name = "immagine", nullable = false)
    private String immagine;

    @OneToMany(mappedBy = "prodotto")
    @JsonIgnore
    @ToString.Exclude
    private List<Prodotti_Carrello> carrelli;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prodotto prodotto = (Prodotto) o;
        return id == prodotto.id && Float.compare(prezzo, prodotto.prezzo) == 0 && Objects.equals(nome, prodotto.nome) && Objects.equals(descrizione, prodotto.descrizione) && Objects.equals(categoria, prodotto.categoria);
    }

}


package com.example.gioia.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "prodotti_carrello")
public class Prodotti_Carrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "carrello")
    private Carrello carrello;

    @ManyToOne
    @JoinColumn(name = "prodotto")
    private Prodotto prodotto;

    @Basic
    @Column(name = "quantita", nullable = true)
    private int quantita;

    @Basic
    @Column(name = "prezzo", nullable = true)
    private float prezzo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prodotti_Carrello that = (Prodotti_Carrello) o;
        return id == that.id && quantita == that.quantita && Objects.equals(prodotto, that.prodotto);
    }
}


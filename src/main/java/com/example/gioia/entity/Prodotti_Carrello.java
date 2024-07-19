package com.example.gioia.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


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

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "carrello")
    private Carrello carrello;

    @ManyToOne
    @JoinColumn(name = "prodotto")
    private Prodotto prodotto;

    @Basic
    @Column(name = "quantità", nullable = false)
    private int quantità;




}


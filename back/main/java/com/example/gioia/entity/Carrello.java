package com.example.gioia.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
@Entity
@Table(name = "Carrello")
public class Carrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente;

    @OneToMany(mappedBy ="carrello")
    private List<Prodotti_Carrello> prodotti;

    @Version
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "version")
    private long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrello carrello = (Carrello) o;
        return Objects.equals(cliente, carrello.cliente) && prodotti.toString().equals(carrello.prodotti.toString());//TODO

    }

}

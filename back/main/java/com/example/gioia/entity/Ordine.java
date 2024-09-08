package com.example.gioia.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "Ordine")
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "carrello")
    private Carrello carrello;

    @Basic
    @Column(name="indirizzo", nullable = false)
    private String indirizzo;

    @Basic
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ordine ordine = (Ordine) o;
        return id == ordine.id && Objects.equals(carrello, ordine.carrello) && Objects.equals(indirizzo, ordine.indirizzo) && Objects.equals(data, ordine.data) && Objects.equals(cliente, ordine.cliente);
    }
}

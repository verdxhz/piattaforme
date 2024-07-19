package com.example.gioia.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "Carrello")
public class Carrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente;

    @Basic
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm")
    @Column(name = "data", nullable = false)
    private Date data;

    @OneToMany(mappedBy ="carrello")
    private List<Prodotti_Carrello> prodotti;
}

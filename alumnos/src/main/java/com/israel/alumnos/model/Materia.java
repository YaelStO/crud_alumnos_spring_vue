package com.israel.alumnos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreMateria;
    private Integer creditos;

    // Relación 1 a 1: Una materia tiene un horario específico
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "horario_id", referencedColumnName = "id")
    private Horario horario;

    // Relación Muchos a 1: Un docente puede impartir muchas materias
    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Docente docente;
}
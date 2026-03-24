package com.israel.alumnos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroControl;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String carrera;
    private String imagenURL;

    // Relación Muchos a Muchos: Un alumno toma varias materias, una materia tiene varios alumnos
    @ManyToMany
    @JoinTable(
            name = "alumno_materia",
            joinColumns = @JoinColumn(name = "alumno_id"),
            inverseJoinColumns = @JoinColumn(name = "materia_id")
    )
    private List<Materia> materias;
}
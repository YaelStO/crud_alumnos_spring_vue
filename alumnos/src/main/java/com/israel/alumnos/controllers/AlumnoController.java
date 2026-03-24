package com.israel.alumnos.controllers;

import com.israel.alumnos.model.Alumno;
import com.israel.alumnos.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoController {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @GetMapping
    public List<Alumno> getAll() {
        return alumnoRepository.findAll();
    }

    @PostMapping
    public Alumno create(@RequestBody Alumno alumno) {
        return alumnoRepository.save(alumno);
    }

    // NUEVO: Método para actualizar (Editar)
    @PutMapping("/{id}")
    public Alumno update(@PathVariable Long id, @RequestBody Alumno alumno) {
        alumno.setId(id); // Asegura que se actualice el registro correcto
        return alumnoRepository.save(alumno);
    }

    // NUEVO: Método para eliminar
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        alumnoRepository.deleteById(id);
    }
}

package com.israel.alumnos.controllers;

import com.israel.alumnos.model.Materia;
import com.israel.alumnos.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@CrossOrigin(origins = "*")
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;

    @GetMapping
    public List<Materia> getAll() {
        return materiaRepository.findAll();
    }

    @PostMapping
    public Materia create(@RequestBody Materia materia) {
        return materiaRepository.save(materia);
    }

    // NUEVO: Metodo para actuzalizar
    @PutMapping("/{id}")
    public Materia update(@PathVariable Long id, @RequestBody Materia materia) {
        materia.setId(id);
        return materiaRepository.save(materia);
    }

    // Metodod para eliminar
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        materiaRepository.deleteById(id);
    }
}
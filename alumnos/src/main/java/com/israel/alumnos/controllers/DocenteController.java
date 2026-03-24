package com.israel.alumnos.controllers;

import com.israel.alumnos.model.Docente;
import com.israel.alumnos.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "*")
public class DocenteController {

    @Autowired
    private DocenteRepository docenteRepository;

    @GetMapping
    public List<Docente> getAll() {
        return docenteRepository.findAll();
    }

    @PostMapping
    public Docente create(@RequestBody Docente docente) {
        return docenteRepository.save(docente);
    }

    // NUEVO: Método para actualizar (Editar)
    @PutMapping("/{id}")
    public Docente update(@PathVariable Long id, @RequestBody Docente docente) {
        docente.setId(id);
        return docenteRepository.save(docente);
    }

    // NUEVO: Método para eliminar
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        docenteRepository.deleteById(id);
    }
}
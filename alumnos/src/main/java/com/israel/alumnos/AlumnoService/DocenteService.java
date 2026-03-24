package com.israel.alumnos.AlumnoService;

import com.israel.alumnos.model.Docente;
import com.israel.alumnos.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    public List<Docente> obtenerTodos() {
        return docenteRepository.findAll();
    }

    public Optional<Docente> obtenerPorId(Long id) {
        return docenteRepository.findById(id);
    }

    public Docente guardarDocente(Docente docente) {
        return docenteRepository.save(docente);
    }

    public Optional<Docente> actualizarDocente(Long id, Docente detalles) {
        return docenteRepository.findById(id).map(existente -> {
            existente.setNombre(detalles.getNombre());
            existente.setDepartamento(detalles.getDepartamento());
            existente.setNumeroEmpleado(detalles.getNumeroEmpleado());
            return docenteRepository.save(existente);
        });
    }

    public void eliminarDocente(Long id) {
        docenteRepository.deleteById(id);
    }
}
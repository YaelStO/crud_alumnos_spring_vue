package com.israel.alumnos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.israel.alumnos.controllers.AlumnoController;
import com.israel.alumnos.model.Alumno;
import com.israel.alumnos.AlumnoService.AlumnoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlumnoController.class)
public class AlumnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumnoService alumnoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void debeTraerTodosLosAlumnos() throws Exception {

        Alumno alumno1 = new Alumno();
        alumno1.setId(1L);
        alumno1.setNombre("Yael");
        alumno1.setCarrera("Sistemas");

        Alumno alumno2 = new Alumno();
        alumno2.setId(2L);
        alumno2.setNombre("Carmen");
        alumno2.setCarrera("Civil");

        when(alumnoService.obtenerTodos()).thenReturn(Arrays.asList(alumno1, alumno2));

        mockMvc.perform(get("/alumnos/traer-alumnos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Yael")));
    }

    @Test
    public void debeInsertarUnAlumno() throws Exception {

        Alumno alumnoNuevo = new Alumno();
        alumnoNuevo.setNombre("YAEL");
        alumnoNuevo.setNumeroControl("21620147");

        when(alumnoService.guardarAlumno(any(Alumno.class))).thenReturn(alumnoNuevo);

        mockMvc.perform(post("/alumnos/insertar-alumnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alumnoNuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("YAEL")));
    }

    @Test
    public void debeEditarUnAlumno() throws Exception {

        Long id = 1L;

        Alumno alumnoEditado = new Alumno();
        alumnoEditado.setId(id);
        alumnoEditado.setNombre("Yael de Jesus");
        alumnoEditado.setCarrera("Ingenieria en Sistemas");
        alumnoEditado.setNumeroControl("21620147");

        when(alumnoService.actualizarAlumno(eq(id), any(Alumno.class)))
                .thenReturn(Optional.of(alumnoEditado));

        mockMvc.perform(put("/alumnos/editar-alumnos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alumnoEditado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Yael de Jesus")))
                .andExpect(jsonPath("$.carrera", is("Ingenieria en Sistemas")));
    }

    @Test
    public void debeEliminarUnAlumno() throws Exception {

        Long id = 1L;

        doNothing().when(alumnoService).eliminarAlumno(id);

        mockMvc.perform(delete("/alumnos/eliminar-alumnos/{id}", id))
                .andExpect(status().isOk());

        verify(alumnoService, times(1)).eliminarAlumno(id);
    }
}
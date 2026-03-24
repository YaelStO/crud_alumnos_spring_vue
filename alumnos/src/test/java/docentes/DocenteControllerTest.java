package com.israel.alumnos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.israel.alumnos.controllers.DocenteController;
import com.israel.alumnos.model.Docente;
import com.israel.alumnos.AlumnoService.DocenteService;
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

@WebMvcTest(DocenteController.class)
public class DocenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 1. Cambiamos el Mock de Repository por el de Service
    @MockBean
    private DocenteService docenteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void debeTraerTodosLosDocentes() throws Exception {
        Docente docente1 = new Docente(1L, "Israel", "Desarrollo Agil", "EMP-001");
        Docente docente2 = new Docente(2L, "Lucia", "Metodos Numericos", "EMP-002");

        when(docenteService.obtenerTodos()).thenReturn(Arrays.asList(docente1, docente2));

        mockMvc.perform(get("/docentes/traer-docentes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Israel")));
    }

    @Test
    public void debeInsertarUnDocente() throws Exception {
        Docente docenteNuevo = new Docente();
        docenteNuevo.setNombre("Israel");
        docenteNuevo.setNumeroEmpleado("EMP-1029");

        when(docenteService.guardarDocente(any(Docente.class))).thenReturn(docenteNuevo);

        mockMvc.perform(post("/docentes/insertar-docente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docenteNuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Israel")));
    }

    @Test
    public void debeEditarUnDocente() throws Exception {
        Docente docenteEditado = new Docente();
        docenteEditado.setId(1L);
        docenteEditado.setNombre("Roberto Carlos");
        docenteEditado.setNumeroEmpleado("EMP-1029");
        docenteEditado.setDepartamento("Ciencias Básicas");

        when(docenteService.actualizarDocente(eq(1L), any(Docente.class)))
                .thenReturn(Optional.of(docenteEditado));

        mockMvc.perform(put("/docentes/editar-docente/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docenteEditado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Roberto Carlos")))
                .andExpect(jsonPath("$.departamento", is("Ciencias Básicas")));

        verify(docenteService).actualizarDocente(eq(1L), any(Docente.class));
    }

    @Test
    public void debeEliminarUnDocente() throws Exception {
        Long idParaEliminar = 1L;

        doNothing().when(docenteService).eliminarDocente(idParaEliminar);

        mockMvc.perform(delete("/docentes/eliminar-docente/{id}", idParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(docenteService, times(1)).eliminarDocente(idParaEliminar);
    }
}
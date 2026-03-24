<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

const BASE_URL = 'http://localhost:8080/api';
const vistaActual = ref('alumnos');
const alumnos = ref([]);
const docentes = ref([]);
const materias = ref([]);
const editado = ref(false);

// Estructura completa basada en tu Base de Datos
const item = ref({
  id: null,
  // Campos de Alumno
  nombre: '',
  apellido: '',
  carrera: '',
  telefono: '',
  email: '',
  numeroControl: '',
  imagenurl: '',
  // Campos de Docente
  departamento: '',
  numeroEmpleado: '',
  // Campos de Materia
  nombreMateria: '',
  creditos: null,
  docente: null,
  // Objeto anidado para Horario (Relación 1 a 1 en Materia)
  horario: {
    aula: '',
    diaSemana: '',
    horaInicio: '',
    horaFin: ''
  }
});

const cargarTodo = async () => {
  try {
    const [resAlum, resDoc, resMat] = await Promise.all([
      axios.get(`${BASE_URL}/alumnos`),
      axios.get(`${BASE_URL}/docentes`),
      axios.get(`${BASE_URL}/materias`)
    ]);
    alumnos.value = resAlum.data;
    docentes.value = resDoc.data;
    materias.value = resMat.data;
  } catch (e) {
    console.error("Error al cargar datos", e);
  }
};

const guardar = async () => {
  try {
    const url = `${BASE_URL}/${vistaActual.value}`;

    // Clonamos el item para enviarlo limpio
    const payload = JSON.parse(JSON.stringify(item.value));

    if (editado.value) {
      await axios.put(`${url}/${payload.id}`, payload);
      Swal.fire('Actualizado', 'Registro actualizado correctamente', 'success');
    } else {
      await axios.post(url, payload);
      Swal.fire('Guardado', 'Registro creado con éxito', 'success');
    }
    limpiar();
    await cargarTodo();
  } catch (e) {
    console.error(e);
    Swal.fire('Error', 'Hubo un problema al guardar. Verifica la consola.', 'error');
  }
};

const eliminar = async (id) => {
  const result = await Swal.fire({
    title: '¿Estás seguro?',
    text: "Esta acción eliminará el registro de la base de datos.",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sí, eliminar'
  });

  if (result.isConfirmed) {
    try {
      await axios.delete(`${BASE_URL}/${vistaActual.value}/${id}`);
      Swal.fire('Eliminado', 'El registro ha sido borrado', 'success');
      await cargarTodo();
    } catch (e) {
      Swal.fire('Error', 'No se pudo eliminar. Puede que esté relacionado con otro registro.', 'error');
    }
  }
};

const prepararEdicion = (objeto) => {
  // Uso JSON parse/stringify para romper la reactividad y no editar la tabla en vivo
  item.value = JSON.parse(JSON.stringify(objeto));

  // Si la materia no tiene horario registrado, inicializamos el objeto para evitar errores en el v-model
  if (vistaActual.value === 'materias' && !item.value.horario) {
    item.value.horario = { aula: '', diaSemana: '', horaInicio: '', horaFin: '' };
  }

  editado.value = true;
};

const limpiar = () => {
  item.value = {
    id: null,
    nombre: '', apellido: '', carrera: '', telefono: '', email: '', numeroControl: '', imagenurl: '',
    departamento: '', numeroEmpleado: '',
    nombreMateria: '', creditos: null, docente: null,
    horario: { aula: '', diaSemana: '', horaInicio: '', horaFin: '' }
  };
  editado.value = false;
};

// Cambiar de pestaña
const cambiarVista = (vista) => {
  vistaActual.value = vista;
  limpiar();
};

onMounted(cargarTodo);
</script>

<template>
  <div class="container py-4">
    <ul class="nav nav-pills nav-fill mb-4 shadow-sm p-2 bg-white rounded">
      <li class="nav-item">
        <button class="nav-link" :class="{active: vistaActual === 'alumnos'}" @click="cambiarVista('alumnos')">Alumnos</button>
      </li>
      <li class="nav-item">
        <button class="nav-link" :class="{active: vistaActual === 'docentes'}" @click="cambiarVista('docentes')">Docentes</button>
      </li>
      <li class="nav-item">
        <button class="nav-link" :class="{active: vistaActual === 'materias'}" @click="cambiarVista('materias')">Materias</button>
      </li>
    </ul>

    <div class="card shadow p-4 mb-4">
      <h3 class="mb-4 text-primary">{{ editado ? 'Editar' : 'Registrar' }} {{ vistaActual.slice(0, -1).toUpperCase() }}</h3>
      <form @submit.prevent="guardar">
        <div class="row g-3">

          <template v-if="vistaActual === 'alumnos'">
            <div class="col-md-4">
              <label class="form-label">Número de Control</label>
              <input type="text" class="form-control" v-model="item.numeroControl" required>
            </div>
            <div class="col-md-4">
              <label class="form-label">Nombre</label>
              <input type="text" class="form-control" v-model="item.nombre" required>
            </div>
            <div class="col-md-4">
              <label class="form-label">Apellidos</label>
              <input type="text" class="form-control" v-model="item.apellido" required>
            </div>
            <div class="col-md-4">
              <label class="form-label">Carrera</label>
              <input type="text" class="form-control" v-model="item.carrera" required>
            </div>
            <div class="col-md-4">
              <label class="form-label">Email</label>
              <input type="email" class="form-control" v-model="item.email">
            </div>
            <div class="col-md-4">
              <label class="form-label">Teléfono</label>
              <input type="text" class="form-control" v-model="item.telefono">
            </div>
            <div class="col-md-12">
              <label class="form-label">URL Fotografía</label>
              <input type="text" class="form-control" v-model="item.imagenurl">
            </div>
          </template>

          <template v-if="vistaActual === 'docentes'">
            <div class="col-md-4">
              <label class="form-label">Número de Empleado</label>
              <input type="text" class="form-control" v-model="item.numeroEmpleado" required>
            </div>
            <div class="col-md-4">
              <label class="form-label">Nombre Completo</label>
              <input type="text" class="form-control" v-model="item.nombre" required>
            </div>
            <div class="col-md-4">
              <label class="form-label">Departamento</label>
              <input type="text" class="form-control" v-model="item.departamento" required>
            </div>
          </template>

          <template v-if="vistaActual === 'materias'">
            <div class="col-md-4">
              <label class="form-label">Nombre de la Materia</label>
              <input type="text" class="form-control" v-model="item.nombreMateria" required>
            </div>
            <div class="col-md-2">
              <label class="form-label">Créditos</label>
              <input type="number" class="form-control" v-model="item.creditos" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Docente que la imparte</label>
              <select class="form-select" v-model="item.docente" required>
                <option :value="null" disabled>Seleccione un docente...</option>
                <option v-for="doc in docentes" :key="doc.id" :value="{ id: doc.id }">
                  {{ doc.nombre }} ({{ doc.departamento }})
                </option>
              </select>
            </div>

            <h5 class="mt-4 text-secondary">Horario de la Materia</h5>
            <div class="col-md-3">
              <label class="form-label">Aula</label>
              <input type="text" class="form-control" v-model="item.horario.aula" required>
            </div>
            <div class="col-md-3">
              <label class="form-label">Día de la Semana</label>
              <select class="form-select" v-model="item.horario.diaSemana" required>
                <option value="Lunes">Lunes</option>
                <option value="Martes">Martes</option>
                <option value="Miércoles">Miércoles</option>
                <option value="Jueves">Jueves</option>
                <option value="Viernes">Viernes</option>
                <option value="Sábado">Sábado</option>
              </select>
            </div>
            <div class="col-md-3">
              <label class="form-label">Hora Inicio (Ej. 08:00)</label>
              <input type="time" class="form-control" v-model="item.horario.horaInicio" required>
            </div>
            <div class="col-md-3">
              <label class="form-label">Hora Fin (Ej. 10:00)</label>
              <input type="time" class="form-control" v-model="item.horario.horaFin" required>
            </div>
          </template>
        </div>

        <div class="mt-4">
          <button type="submit" class="btn btn-primary me-2 px-4">
            <i class="bi" :class="editado ? 'bi-save' : 'bi-plus-circle'"></i> {{ editado ? 'Actualizar Datos' : 'Registrar' }}
          </button>
          <button type="button" v-if="editado" class="btn btn-secondary px-4" @click="limpiar">Cancelar</button>
        </div>
      </form>
    </div>

    <div class="card shadow p-3">
      <div class="table-responsive">
        <table class="table table-hover align-middle text-center">
          <thead class="table-dark">
            <tr v-if="vistaActual === 'alumnos'">
              <th>No. Control</th><th>Foto</th><th>Nombre</th><th>Carrera</th><th>Email</th><th>Acciones</th>
            </tr>
            <tr v-if="vistaActual === 'docentes'">
              <th>No. Empleado</th><th>Nombre</th><th>Departamento</th><th>Acciones</th>
            </tr>
            <tr v-if="vistaActual === 'materias'">
              <th>Materia</th><th>Créditos</th><th>Docente</th><th>Horario</th><th>Aula</th><th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="obj in (vistaActual === 'alumnos' ? alumnos : vistaActual === 'docentes' ? docentes : materias)" :key="obj.id">

              <template v-if="vistaActual === 'alumnos'">
                <td><span class="badge bg-secondary">{{ obj.numeroControl }}</span></td>
                <td>
                  <img :src="obj.imagenurl || 'https://via.placeholder.com/40'" class="rounded-circle shadow-sm" width="40" height="40" style="object-fit: cover;">
                </td>
                <td class="text-start">{{ obj.nombre }} {{ obj.apellido }}</td>
                <td>{{ obj.carrera }}</td>
                <td>{{ obj.email }}</td>
              </template>

              <template v-if="vistaActual === 'docentes'">
                <td><span class="badge bg-info text-dark">{{ obj.numeroEmpleado }}</span></td>
                <td class="text-start">{{ obj.nombre }}</td>
                <td>{{ obj.departamento }}</td>
              </template>

              <template v-if="vistaActual === 'materias'">
                <td class="text-start fw-bold">{{ obj.nombreMateria }}</td>
                <td>{{ obj.creditos }}</td>
                <td>{{ obj.docente?.nombre || 'Sin asignar' }}</td>
                <td>
                  <span v-if="obj.horario">{{ obj.horario.diaSemana }} de {{ obj.horario.horaInicio }} a {{ obj.horario.horaFin }}</span>
                  <span v-else class="text-muted">Sin horario</span>
                </td>
                <td>{{ obj.horario?.aula || 'N/A' }}</td>
              </template>

              <td>
                <button @click="prepararEdicion(obj)" class="btn btn-warning btn-sm me-2 text-white" title="Editar">
                  <i class="bi bi-pencil-square"></i>
                </button>
                <button @click="eliminar(obj.id)" class="btn btn-danger btn-sm" title="Eliminar">
                  <i class="bi bi-trash3"></i>
                </button>
              </td>

            </tr>
            <tr v-if="(vistaActual === 'alumnos' ? alumnos : vistaActual === 'docentes' ? docentes : materias).length === 0">
              <td colspan="6" class="text-muted py-4">No hay registros almacenados.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.nav-link { cursor: pointer; font-weight: 500; }
.nav-pills .nav-link.active { background-color: #0d6efd; color: white; }
.nav-link { color: #495057; }
</style>
package com.valva.proyectointegrador.controllers.impl;

import com.valva.proyectointegrador.controllers.CRUDController;
import com.valva.proyectointegrador.exceptions.BadRequestException;
import com.valva.proyectointegrador.exceptions.ResourceNotFoundException;
import com.valva.proyectointegrador.model.PacienteDto;
import com.valva.proyectointegrador.service.IPacienteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController implements CRUDController<PacienteDto> {

    @Qualifier("pacienteService")
    private final IPacienteService pacienteService;

    @Autowired
    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @Override
    @ApiOperation(value = "Crea un nuevo paciente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @PostMapping()
    public ResponseEntity<PacienteDto> registrar(@RequestBody PacienteDto paciente) throws BadRequestException, ResourceNotFoundException {
        paciente.setFechaIngreso(LocalDate.now());
        PacienteDto pacienteInsertado = pacienteService.crear(paciente);
        return ResponseEntity.ok(pacienteInsertado);
    }

    @Override
    @ApiOperation(value = "Busca un paciente por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        PacienteDto paciente = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping(params = "dni")
    @ApiOperation(value = "Busca un paciente por DNI")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    public ResponseEntity<PacienteDto> buscar(@RequestParam Integer dni) throws BadRequestException, ResourceNotFoundException {
        PacienteDto paciente = pacienteService.buscar(dni);
        return ResponseEntity.ok(paciente);
    }

    @ApiOperation(value = "Busca pacientes por nombre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping(params = "nombre")
    public ResponseEntity<List<PacienteDto>> buscar(@RequestParam String nombre) {
        List<PacienteDto> pacientes = pacienteService.buscar(nombre);
        return ResponseEntity.ok(pacientes);
    }

    @ApiOperation(value = "Busca pacientes por nombre y apellido")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping(params = {"nombre", "apellido"})
    public ResponseEntity<List<PacienteDto>> buscar(@RequestParam String nombre, @RequestParam String apellido) {
        List<PacienteDto> pacientes = pacienteService.buscar(nombre, apellido);
        return ResponseEntity.ok(pacientes);
    }

    @Override
    @ApiOperation(value = "Actualiza un paciente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @PutMapping()
    public ResponseEntity<?> actualizar(@RequestBody PacienteDto paciente) throws BadRequestException, ResourceNotFoundException {
        PacienteDto actualizado = pacienteService.actualizar(paciente);
        return ResponseEntity.ok(actualizado);
    }

    @Override
    @ApiOperation(value = "Elimina un paciente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        pacienteService.eliminar(id);
        return ResponseEntity.ok("Se eliminó el paciente con id " + id);
    }

    @Override
    @ApiOperation(value = "Busca todos los pacientes")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping
    public ResponseEntity<List<PacienteDto>> buscarTodos() {
        return ResponseEntity.ok(pacienteService.consultarTodos());
    }
}

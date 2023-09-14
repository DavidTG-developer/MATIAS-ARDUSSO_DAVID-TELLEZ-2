package com.valva.proyectointegrador.controllers.impl;

import com.valva.proyectointegrador.controllers.ITurnoController;
import com.valva.proyectointegrador.exceptions.BadRequestException;
import com.valva.proyectointegrador.exceptions.ResourceNotFoundException;
import com.valva.proyectointegrador.model.TurnoDto;
import com.valva.proyectointegrador.service.ITurnoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController implements ITurnoController {

    @Qualifier("turnoService")
    private final ITurnoService turnoService;

    @Autowired
    public TurnoController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @Override
    @ApiOperation(value = "Crea un nuevo turno")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @PostMapping()
    public ResponseEntity<?> registrar(@RequestBody TurnoDto turno) throws BadRequestException, ResourceNotFoundException {
        TurnoDto turnoInsertado = turnoService.crear(turno);
        return ResponseEntity.ok(turnoInsertado);
    }

    @Override
    @ApiOperation(value = "Busca un turno por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        TurnoDto turno = turnoService.buscarPorId(id);
        return ResponseEntity.ok(turno);
    }

    @ApiOperation(value = "Busca todos los turnos por nombre y apellido de paciente y odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping(params = {"nombrePaciente", "apellidoPaciente", "nombreOdontologo", "apellidoOdontologo"})
    public ResponseEntity<List<TurnoDto>> buscar(String nombrePaciente, String apellidoPaciente, String nombreOdontologo, String apellidoOdontologo) {
        List<TurnoDto> turnos = turnoService.buscar(nombrePaciente, apellidoPaciente, nombreOdontologo, apellidoOdontologo);
        return ResponseEntity.ok(turnos);
    }


    @ApiOperation(value = "Busca todos los turnos por nombre y apellido de odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping(params = {"nombreOdontologo", "apellidoOdontologo"})
    public ResponseEntity<List<TurnoDto>> buscar(String nombreOdontologo, String apellidoOdontologo) {
        List<TurnoDto> turnos = turnoService.buscar(nombreOdontologo, apellidoOdontologo);
        return ResponseEntity.ok(turnos);
    }

    @ApiOperation(value = "Busca todos los turnos por DNI del paciente y matrícula del odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping(params = {"matricula", "dni"})
    public ResponseEntity<List<TurnoDto>> buscar(Integer matricula, Integer dni) {
        List<TurnoDto> turnos = turnoService.buscar(matricula, dni);
        return ResponseEntity.ok(turnos);
    }

    @Override
    @ApiOperation(value = "Actualiza un turno")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @PutMapping()
    public ResponseEntity<?> actualizar(@RequestBody TurnoDto turno) throws BadRequestException, ResourceNotFoundException {
        TurnoDto actualizado = turnoService.actualizar(turno);
        return ResponseEntity.ok(actualizado);
    }

    @Override
    @ApiOperation(value = "Elimina un turno")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        turnoService.eliminar(id);
        return ResponseEntity.ok("Se elimino el turno con id " + id);
    }

    @Override
    @ApiOperation(value = "Busca todos los turnos")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping
    public ResponseEntity<List<TurnoDto>> buscarTodos() {
        return ResponseEntity.ok(turnoService.consultarTodos());
    }

    @Override
    @ApiOperation(value = "Busca todos los turnos de la próxima semana")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping("/proximos")
    public ResponseEntity<List<TurnoDto>> buscarTurnosDesde(
                                        @RequestParam Integer dia,
                                        @RequestParam Integer mes,
                                        @RequestParam Integer anio,
                                        @RequestParam(defaultValue = "0") Integer hora,
                                        @RequestParam(defaultValue = "0") Integer minuto,
                                        @RequestParam(defaultValue = "7") Integer cantidadDias) {
        LocalDateTime desde = LocalDateTime.of(anio, mes, dia, hora, minuto);
        List<TurnoDto> turnos = turnoService.consultarProximosTurnos(desde, cantidadDias);
        return ResponseEntity.ok(turnos);
    }
}

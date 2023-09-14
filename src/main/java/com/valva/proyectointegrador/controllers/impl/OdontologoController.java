package com.valva.proyectointegrador.controllers.impl;

import com.valva.proyectointegrador.controllers.CRUDController;
import com.valva.proyectointegrador.exceptions.BadRequestException;
import com.valva.proyectointegrador.exceptions.ResourceNotFoundException;
import com.valva.proyectointegrador.model.OdontologoDto;
import com.valva.proyectointegrador.service.IOdontologoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController implements CRUDController<OdontologoDto> {

    @Qualifier("odontologoService")
    private final IOdontologoService odontologoService;

    @Autowired
    public OdontologoController(IOdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    @Override
    @ApiOperation(value = "Crea un nuevo odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @PostMapping()
    public ResponseEntity<OdontologoDto> registrar(@RequestBody OdontologoDto odontologo) throws BadRequestException, ResourceNotFoundException {
        OdontologoDto odontologoInsertado = odontologoService.crear(odontologo);
        return ResponseEntity.ok(odontologoInsertado);
    }

    @Override
    @ApiOperation(value = "Busca un odontólogo por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping("/{id}")
    public ResponseEntity<OdontologoDto> buscarPorId(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        OdontologoDto odontologo = odontologoService.buscarPorId(id);
        return ResponseEntity.ok(odontologo);
    }

    @ApiOperation(value = "Busca un odontólogo por matrícula")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping(params = "matricula")
    public ResponseEntity<OdontologoDto> buscar(@RequestParam Integer matricula) throws ResourceNotFoundException, BadRequestException {
        OdontologoDto odontologo = odontologoService.buscar(matricula);
        return ResponseEntity.ok(odontologo);
    }

    @ApiOperation(value = "Busca odontólogos por nombre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping(params = "nombre")
    public ResponseEntity<List<OdontologoDto>> buscar(@RequestParam String nombre) {
        List<OdontologoDto> odontologos = odontologoService.buscar(nombre);
        return ResponseEntity.ok(odontologos);
    }

    @ApiOperation(value = "Busca odontólogos por nombre y apellido")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping(params = {"nombre", "apellido"})
    public ResponseEntity<List<OdontologoDto>> buscar(@RequestParam String nombre, @RequestParam String apellido) {
        List<OdontologoDto> odontologos = odontologoService.buscar(nombre, apellido);
        return ResponseEntity.ok(odontologos);
    }

    @Override
    @ApiOperation(value = "Actualiza un odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @PutMapping()
    public ResponseEntity<OdontologoDto> actualizar(@RequestBody OdontologoDto odontologo) throws BadRequestException, ResourceNotFoundException {
        OdontologoDto actualizado = odontologoService.actualizar(odontologo);
        return ResponseEntity.ok(actualizado);
    }

    @Override
    @ApiOperation(value = "Elimina un odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request") })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        odontologoService.eliminar(id);
        return ResponseEntity.ok("Se eliminó el odontólogo con id " + id);
    }

    @Override
    @ApiOperation(value = "Busca todos los odontólogos")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping
    public ResponseEntity<List<OdontologoDto>> buscarTodos() {
        return ResponseEntity.ok(odontologoService.consultarTodos());
    }
}

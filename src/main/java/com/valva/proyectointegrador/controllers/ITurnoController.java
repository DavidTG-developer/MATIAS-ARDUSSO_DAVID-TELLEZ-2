package com.valva.proyectointegrador.controllers;

import com.valva.proyectointegrador.model.TurnoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ITurnoController extends CRUDController<TurnoDto> {
    ResponseEntity<List<TurnoDto>> buscarTurnosDesde(@RequestParam(value = "dd") Integer dia,
                                                     @RequestParam(value = "mm") Integer mes,
                                                     @RequestParam(value = "yyyy") Integer anio,
                                                     @RequestParam(value = "hh") Integer horas,
                                                     @RequestParam(value = "ss") Integer segundos,
                                                     @RequestParam(value = "cantDias") Integer cantidadDias);
}

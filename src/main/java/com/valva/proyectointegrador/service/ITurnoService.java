package com.valva.proyectointegrador.service;

import com.valva.proyectointegrador.model.TurnoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ITurnoService extends CRUDService<TurnoDto> {
    List<TurnoDto> buscar(String nombrePaciente, String apellidoPaciente, String nombreOdontologo, String apellidoOdontologo);

    List<TurnoDto> buscar(String nombreOdontologo, String apellidoOdontologo);

    List<TurnoDto> buscar(Integer matricula, Integer dni);

    List<TurnoDto> consultarProximosTurnos(LocalDateTime desde, Integer cantidadDias);
}

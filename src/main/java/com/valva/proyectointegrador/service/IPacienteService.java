package com.valva.proyectointegrador.service;

import com.valva.proyectointegrador.exceptions.BadRequestException;
import com.valva.proyectointegrador.exceptions.ResourceNotFoundException;
import com.valva.proyectointegrador.model.PacienteDto;

import java.util.List;

public interface IPacienteService extends CRUDService<PacienteDto> {
    PacienteDto buscar(Integer dni) throws ResourceNotFoundException, BadRequestException;

    List<PacienteDto> buscar(String nombre);

    List<PacienteDto> buscar(String nombre, String apellido);
}

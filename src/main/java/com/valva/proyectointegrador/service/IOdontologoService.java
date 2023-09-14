package com.valva.proyectointegrador.service;

import com.valva.proyectointegrador.exceptions.BadRequestException;
import com.valva.proyectointegrador.exceptions.ResourceNotFoundException;
import com.valva.proyectointegrador.model.OdontologoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOdontologoService extends CRUDService<OdontologoDto> {
    OdontologoDto buscar(Integer matricula) throws ResourceNotFoundException, BadRequestException;

    List<OdontologoDto> buscar(String nombre);

    List<OdontologoDto> buscar(String nombre, String apellido);
}

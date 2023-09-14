package com.valva.proyectointegrador.service.impl;

import com.valva.proyectointegrador.config.SpringConfig;
import com.valva.proyectointegrador.exceptions.BadRequestException;
import com.valva.proyectointegrador.exceptions.ResourceNotFoundException;
import com.valva.proyectointegrador.model.OdontologoDto;
import com.valva.proyectointegrador.persistence.entities.Odontologo;
import com.valva.proyectointegrador.persistence.repository.IOdontologoRepository;
import com.valva.proyectointegrador.service.IOdontologoService;
import com.valva.proyectointegrador.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OdontologoService implements IOdontologoService {

    private final IOdontologoRepository odontologoRepository;
    private final SpringConfig springConfig;

    @Autowired
    public OdontologoService(IOdontologoRepository odontologoRepository, SpringConfig springConfig) {
        this.odontologoRepository = odontologoRepository;
        this.springConfig = springConfig;
    }

    @Override
    public OdontologoDto buscar(Integer matricula) throws ResourceNotFoundException, BadRequestException {
        if (matricula == null)
            throw new BadRequestException("La matrícula del odontólogo no puede ser null");
        Odontologo odontologo = odontologoRepository.buscar(matricula).orElse(null);
        if (odontologo == null)
            throw new ResourceNotFoundException("El odontólogo con matrícula " + matricula + " no existe");
        return springConfig.getModelMapper().map(odontologo, OdontologoDto.class);
    }

    @Override
    public List<OdontologoDto> buscar(String nombre) {
        List<Odontologo> odontologos = odontologoRepository.buscar(nombre).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), odontologos, OdontologoDto.class);
    }

    @Override
    public List<OdontologoDto> buscar(String nombre, String apellido) {
        List<Odontologo> odontologos = odontologoRepository.buscar(nombre, apellido).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), odontologos, OdontologoDto.class);
    }

    @Override
    public OdontologoDto buscarPorId(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del odontólogo no puede ser null ni negativo");
        Odontologo odontologo = odontologoRepository.findById(id).orElse(null);
        if (odontologo == null)
            throw new ResourceNotFoundException("El odontólogo no existe");
        return springConfig.getModelMapper().map(odontologo, OdontologoDto.class);
    }

    @Override
    public OdontologoDto crear(OdontologoDto odontologoDto) throws BadRequestException {
        if (odontologoDto == null)
            throw new BadRequestException("El odontólogo no puede ser null");
        Odontologo odontologo = springConfig.getModelMapper().map(odontologoDto, Odontologo.class);
        return springConfig.getModelMapper().map(odontologoRepository.save(odontologo), OdontologoDto.class);
    }

    @Override
    public OdontologoDto actualizar(OdontologoDto odontologoDto) throws BadRequestException, ResourceNotFoundException {
        OdontologoDto odontologoActualizado;
        if (odontologoDto == null)
            throw new BadRequestException("El odontólogo no puede ser null");
        if (odontologoDto.getId() == null)
            throw new BadRequestException("El id del odontólogo no puede ser null");
        Optional<Odontologo> odontologoEnBD = odontologoRepository.findById(odontologoDto.getId());
        if (odontologoEnBD.isPresent()) {
            Odontologo actualizado = this.actualizar(odontologoEnBD.get(), odontologoDto);
            Odontologo guardado = odontologoRepository.save(actualizado);
            odontologoActualizado = springConfig.getModelMapper().map(guardado, OdontologoDto.class);
        } else {
            throw new ResourceNotFoundException("El odontólogo no existe");
        }
        return odontologoActualizado;
    }

    @Override
    public void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del odontólogo no puede ser null ni negativo");
        if (!odontologoRepository.existsById(id))
            throw new ResourceNotFoundException("No existe ningún odontólogo con id: " + id);
        odontologoRepository.deleteById(id);
    }

    @Override
    public List<OdontologoDto> consultarTodos() {
        List<Odontologo> odontologos = odontologoRepository.findAll();
        return Mapper.mapList(springConfig.getModelMapper(), odontologos, OdontologoDto.class);
    }

    private Odontologo actualizar(Odontologo odontologo, OdontologoDto odontologoDto) {
        if (odontologoDto.getNombre() != null) {
            odontologo.setNombre(odontologoDto.getNombre());
        }
        if (odontologoDto.getApellido() != null) {
            odontologo.setApellido(odontologoDto.getApellido());
        }
        if (odontologoDto.getMatricula() != null) {
            odontologo.setMatricula(odontologoDto.getMatricula());
        }
        return odontologo;
    }
}

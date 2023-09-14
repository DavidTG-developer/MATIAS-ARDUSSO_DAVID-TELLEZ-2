package com.valva.proyectointegrador.service.impl;

import com.valva.proyectointegrador.config.SpringConfig;
import com.valva.proyectointegrador.exceptions.BadRequestException;
import com.valva.proyectointegrador.exceptions.ResourceNotFoundException;
import com.valva.proyectointegrador.model.OdontologoDto;
import com.valva.proyectointegrador.model.PacienteDto;
import com.valva.proyectointegrador.model.TurnoDto;
import com.valva.proyectointegrador.persistence.entities.Turno;
import com.valva.proyectointegrador.persistence.repository.ITurnoRepository;
import com.valva.proyectointegrador.service.CRUDService;
import com.valva.proyectointegrador.service.ITurnoService;
import com.valva.proyectointegrador.utils.Mapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurnoService implements ITurnoService {

    @Autowired
    private CRUDService<OdontologoDto> odontologoService;
    @Autowired
    private CRUDService<PacienteDto> pacienteService;
    private final ITurnoRepository turnoRepository;
    @Autowired
    private SpringConfig springConfig;

    @Autowired
    public TurnoService(ITurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @Override
    public List<TurnoDto> buscar(String nombrePaciente, String apellidoPaciente, String nombreOdontologo, String apellidoOdontologo) {
        List<Turno> turnos = turnoRepository.buscar(nombrePaciente, apellidoPaciente, nombreOdontologo, apellidoOdontologo).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), turnos, TurnoDto.class);
    }

    @Override
    public List<TurnoDto> buscar(String nombreOdontologo, String apellidoOdontologo) {
        List<Turno> turnos = turnoRepository.buscar(nombreOdontologo, apellidoOdontologo).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), turnos, TurnoDto.class);
    }

    @Override
    public List<TurnoDto> buscar(Integer matricula, Integer dni) {
        List<Turno> turnos = turnoRepository.buscar(matricula, dni).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), turnos, TurnoDto.class);
    }

    @Override
    public TurnoDto buscarPorId(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del turno no puede ser null ni negativo");
        Turno turno = turnoRepository.findById(id).orElse(null);
        if (turno == null)
            throw new ResourceNotFoundException("No se encontró el turno con id " + id);
        return Mapper.map(springConfig.getModelMapper(), turno, TurnoDto.class);
    }

    @Override
    public TurnoDto crear(TurnoDto turnoDto) throws BadRequestException, ResourceNotFoundException {
        if (turnoDto.getPaciente() == null || turnoDto.getOdontologo() == null)
            throw new BadRequestException("El paciente u odontólogo es null");
        Integer pacienteId = turnoDto.getPaciente().getId();
        Integer odontologoId = turnoDto.getOdontologo().getId();
        if (this.existenPacienteYOdontologo(pacienteId, odontologoId)) {
            if (this.sePuedeSacarTurno(turnoDto)) {
                Turno turno = Mapper.map(springConfig.getModelMapper(), turnoDto, Turno.class);
                turnoDto = Mapper.map(springConfig.getModelMapper(), turnoRepository.save(turno), TurnoDto.class);
                turnoDto.setPaciente(pacienteService.buscarPorId(pacienteId));
                turnoDto.setOdontologo(odontologoService.buscarPorId(odontologoId));
            } else {
                throw new BadRequestException("El odontólogo ya tiene un turno programado para ese día en ese horario");
            }
        } else {
            throw new BadRequestException("El paciente u odontólogo no existen");
        }
        return turnoDto;
    }

    @SneakyThrows
    @Override
    public TurnoDto actualizar(TurnoDto turnoDto) {
        TurnoDto turnoActualizado;
        if (turnoDto == null)
            throw new BadRequestException("No se pudo actualizar el turno null");
        if (turnoDto.getId() == null)
            throw new BadRequestException("El id del turno a actualizar no puede ser null");
        if (turnoDto.getPaciente() == null || turnoDto.getOdontologo() == null)
            throw new BadRequestException("El paciente u odontólogo es null");
        Optional<Turno> turnoEnBD = turnoRepository.findById(turnoDto.getId());

        if (turnoEnBD.isPresent()) {
            if (this.sePuedeSacarTurno(turnoDto)) {
                Turno actualizado = this.actualizar(turnoEnBD.get(), turnoDto);
                Turno guardado = turnoRepository.save(actualizado);
                turnoActualizado = Mapper.map(springConfig.getModelMapper(), guardado, TurnoDto.class);
            } else {
                throw new BadRequestException("El odontólogo ya tiene un turno programado para ese día en ese horario");
            }
        } else {
            throw new ResourceNotFoundException("El odontólogo no existe");
        }
        return turnoActualizado;
    }

    @Override
    public void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del turno no puede ser null ni negativo");
        if (!turnoRepository.existsById(id))
            throw new ResourceNotFoundException("No existe ningún turno con id: " + id);
        turnoRepository.deleteById(id);
    }

    @Override
    public List<TurnoDto> consultarTodos() {
        List<Turno> turnos = turnoRepository.findAll();
        return Mapper.mapList(springConfig.getModelMapper(), turnos, TurnoDto.class);
    }

    @Override
    public List<TurnoDto> consultarProximosTurnos(LocalDateTime desde, Integer cantidadDias) {
        LocalDateTime hasta = desde.plusDays(cantidadDias);
        List<Turno> filtrados = turnoRepository.findAll()
                .stream()
                .filter(turno -> ((turno.getFecha().isAfter(desde) || turno.getFecha().equals(desde)) && turno.getFecha().isBefore(hasta)))
                .collect(Collectors.toList());
        return Mapper.mapList(springConfig.getModelMapper(), filtrados, TurnoDto.class);
    }

    private Turno actualizar(Turno turno, TurnoDto turnoDto) throws Exception {
        if (turnoDto.getFecha() != null) {
            turno.setFecha(turnoDto.getFecha());
        }
        if (turnoDto.getPaciente() != null) {
            pacienteService.actualizar(turnoDto.getPaciente());
        }
        if (turnoDto.getOdontologo() != null) {
            odontologoService.actualizar(turnoDto.getOdontologo());
        }
        return turno;
    }

    private boolean existenPacienteYOdontologo(Integer pacienteId, Integer odontologoId) throws BadRequestException, ResourceNotFoundException {
        PacienteDto p = pacienteService.buscarPorId(pacienteId);
        OdontologoDto o = odontologoService.buscarPorId(odontologoId);
        return (p != null && o != null);
    }

    private boolean sePuedeSacarTurno(TurnoDto turnoDto) throws BadRequestException, ResourceNotFoundException {
        OdontologoDto odontologoDto = odontologoService.buscarPorId(turnoDto.getOdontologo().getId());
        return turnoRepository.findAll()
                .stream()
                .map(turno -> Mapper.map(springConfig.getModelMapper(), turno, TurnoDto.class))
                .noneMatch(t -> t.getOdontologo().equals(odontologoDto) && t.getFecha().equals(turnoDto.getFecha()));
    }
}

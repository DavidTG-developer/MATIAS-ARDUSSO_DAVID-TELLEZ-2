package com.valva.proyectointegrador.service.impl;

import com.valva.proyectointegrador.exceptions.ResourceNotFoundException;
import com.valva.proyectointegrador.model.DomicilioDto;
import com.valva.proyectointegrador.service.IDomicilioService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class DomicilioServiceTests {

    @Autowired
    private IDomicilioService domicilioService;
    private DomicilioDto domicilio;

    @BeforeEach
    public void setUp() {
        domicilio = new DomicilioDto();
        domicilio.setCalle("Calle Falsa");
        domicilio.setNumero(123);
        domicilio.setLocalidad("Springfield");
        domicilio.setProvincia("Springfield");
    }

    @Test
    public void test01AgregarDomicilio() throws Exception {
        DomicilioDto d = domicilioService.crear(domicilio);
        assertNotNull(domicilioService.buscarPorId(d.getId()));
    }

    @Test
    public void test02ActualizarDomicilio() throws Exception {
        DomicilioDto d = domicilioService.crear(domicilio);
        DomicilioDto original = domicilioService.buscarPorId(d.getId());
        d.setCalle("Falsisima");
        DomicilioDto actualizado = domicilioService.actualizar(d);
        assertNotEquals(actualizado, original);
    }

    @Test
    public void test03EliminarDomicilio() throws Exception {
        DomicilioDto d = domicilioService.crear(domicilio);
        assertNotNull(domicilioService.buscarPorId(d.getId()));
        domicilioService.eliminar(d.getId());
        assertThrows(ResourceNotFoundException.class, () -> domicilioService.buscarPorId(d.getId()));
    }

    @Test
    public void test04ObtenerTodosLosDomicilios() throws Exception {
        domicilioService.crear(domicilio);
        assertNotEquals(0, domicilioService.consultarTodos().size());
    }
}

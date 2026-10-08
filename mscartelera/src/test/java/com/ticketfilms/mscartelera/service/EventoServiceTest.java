package com.ticketfilms.mscartelera.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ticketfilms.mscartelera.model.Evento;
import com.ticketfilms.mscartelera.repository.EventoRepository;

@ExtendWith(MockitoExtension.class)
public class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks 
    private EventoService eventoService;

    @Test
    public void testListarEventos() {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setTitulo("Batman");

        when(eventoRepository.findAll()).thenReturn(List.of(evento));

        List<Evento> eventos = eventoService.listarEventos();

        assertNotNull(eventos);
        assertEquals(1, eventos.size());
        assertEquals("Batman", eventos.get(0).getTitulo());
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    public void testListarEventosFiltrados() {
        Evento evento1 = new Evento();
        evento1.setId(1L);
        evento1.setTitulo("Lollapalooza");
        evento1.setCiudad("Santiago");
        evento1.setRegion("Metropolitana");
        evento1.setTipoEvento("Conciertos");

        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setTitulo("Obra Infantil");
        evento2.setCiudad("Viña del Mar");
        evento2.setRegion("Valparaíso");
        evento2.setTipoEvento("Teatro");

        when(eventoRepository.findAll()).thenReturn(List.of(evento1, evento2));

        List<Evento> filtradosSantiago = eventoService.listarEventosFiltrados("Santiago", null, null);
        assertNotNull(filtradosSantiago);
        assertEquals(1, filtradosSantiago.size());
        assertEquals("Lollapalooza", filtradosSantiago.get(0).getTitulo());

        List<Evento> filtradosTeatroValpo = eventoService.listarEventosFiltrados(null, "Valparaíso", "Teatro");
        assertNotNull(filtradosTeatroValpo);
        assertEquals(1, filtradosTeatroValpo.size());
        assertEquals("Obra Infantil", filtradosTeatroValpo.get(0).getTitulo());

        verify(eventoRepository, times(2)).findAll();
    }
}
package com.ticketfilms.mscartelera.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ticketfilms.mscartelera.model.Evento;
import com.ticketfilms.mscartelera.model.Funcion;
import com.ticketfilms.mscartelera.repository.EventoRepository;
import com.ticketfilms.mscartelera.repository.FuncionRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final FuncionRepository funcionRepository;

    public EventoService(EventoRepository eventoRepository, FuncionRepository funcionRepository) {
        this.eventoRepository = eventoRepository;
        this.funcionRepository = funcionRepository;
    }

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public List<Evento> listarEventosFiltrados(String ciudad, String region, String tipoEvento, String sede, String genero, LocalDateTime fechaDesde) {
        List<Evento> eventos = eventoRepository.findAll();
        
        return eventos.stream()
                .filter(e -> ciudad == null || (e.getCiudad() != null && e.getCiudad().equalsIgnoreCase(ciudad)))
                .filter(e -> region == null || (e.getRegion() != null && e.getRegion().equalsIgnoreCase(region)))
                .filter(e -> tipoEvento == null || (e.getTipoEvento() != null && e.getTipoEvento().equalsIgnoreCase(tipoEvento)))
                .filter(e -> sede == null || (e.getSede() != null && e.getSede().equalsIgnoreCase(sede)))
                .filter(e -> genero == null || (e.getGenero() != null && e.getGenero().equalsIgnoreCase(genero)))
                .filter(e -> fechaDesde == null || tieneFuncionPosterior(e.getId(), fechaDesde))
                .collect(Collectors.toList());
    }

    private boolean tieneFuncionPosterior(Long eventoId, LocalDateTime fechaDesde) {
        return funcionRepository.findByEvento_id(eventoId).stream()
                .anyMatch(f -> f.getFechaHora() != null && f.getFechaHora().isAfter(fechaDesde));
    }

    public Optional<Evento> buscarEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento guardarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public List<String> listarRegiones() {
        return eventoRepository.findAll().stream()
                .map(Evento::getRegion)
                .filter(r -> r != null && !r.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> listarCiudadesPorRegion(String region) {
        return eventoRepository.findAll().stream()
                .filter(e -> e.getRegion() != null && e.getRegion().equalsIgnoreCase(region))
                .map(Evento::getCiudad)
                .filter(c -> c != null && !c.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> listarSedesPorCiudad(String ciudad) {
        return eventoRepository.findAll().stream()
                .filter(e -> e.getCiudad() != null && e.getCiudad().equalsIgnoreCase(ciudad))
                .map(Evento::getSede)
                .filter(s -> s != null && !s.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
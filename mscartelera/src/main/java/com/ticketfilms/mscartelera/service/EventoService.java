package com.ticketfilms.mscartelera.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ticketfilms.mscartelera.model.Evento;
import com.ticketfilms.mscartelera.repository.EventoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public List<Evento> listarEventosFiltrados(String ciudad, String region, String tipoEvento) {
        List<Evento> eventos = eventoRepository.findAll();
        
        return eventos.stream()
                .filter(e -> ciudad == null || (e.getCiudad() != null && e.getCiudad().equalsIgnoreCase(ciudad)))
                .filter(e -> region == null || (e.getRegion() != null && e.getRegion().equalsIgnoreCase(region)))
                .filter(e -> tipoEvento == null || (e.getTipoEvento() != null && e.getTipoEvento().equalsIgnoreCase(tipoEvento)))
                .collect(Collectors.toList());
    }

    public Optional<Evento> buscarEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento guardarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }
}
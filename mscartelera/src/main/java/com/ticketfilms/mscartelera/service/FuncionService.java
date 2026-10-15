package com.ticketfilms.mscartelera.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ticketfilms.mscartelera.model.Funcion;
import com.ticketfilms.mscartelera.repository.FuncionRepository;

@Service
public class FuncionService {

    private final FuncionRepository funcionRepository;

    public FuncionService(FuncionRepository funcionRepository) {
        this.funcionRepository = funcionRepository;
    }

    /**
     * Lista funciones por evento, opcionalmente filtrando desde una fecha/hora.
     * @param eventoId ID del evento
     * @param desde Fecha/hora opcional para no traer funciones pasadas
     * @return Lista de funciones
     */
    public List<Funcion> listarFuncionesPorEvento(Long eventoId, LocalDateTime desde) {
        if (desde != null) {
            return funcionRepository.findByEvento_id(eventoId).stream()
                    .filter(f -> f.getFechaHora() != null && f.getFechaHora().isAfter(desde))
                    .collect(Collectors.toList());
        }
        return funcionRepository.findByEvento_id(eventoId);
    }

    public Optional<Funcion> buscarFuncionPorId(Long id) {
        return funcionRepository.findById(id);
    }

    public Funcion guardarFuncion(Funcion funcion) {
        return funcionRepository.save(funcion);
    }
}
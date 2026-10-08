package com.ticketfilms.mscartelera.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketfilms.mscartelera.model.Evento;
import com.ticketfilms.mscartelera.model.Funcion;
import com.ticketfilms.mscartelera.service.EventoService;
import com.ticketfilms.mscartelera.service.FuncionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cartelera")
@RequiredArgsConstructor
public class CarteleraController {

    private final EventoService eventoService;
    private final FuncionService funcionService;

    @GetMapping("/eventos")
    public ResponseEntity<List<Evento>> listarEventos(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String tipoEvento) {
        
        List<Evento> eventos = eventoService.listarEventosFiltrados(ciudad, region, tipoEvento);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/eventos/{id}")
    public ResponseEntity<Evento> obtenerEventoPorId(@PathVariable Long id) {
        return eventoService.buscarEventoPorId(id)
                .map(this::conProximaFuncion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/funciones/{id}")
    public ResponseEntity<Funcion> obtenerFuncionPorId(@PathVariable Long id) {
        return funcionService.buscarFuncionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/funciones")
    public ResponseEntity<List<Funcion>> listarFuncionesPorEvento(@RequestParam Long eventoId) {
        List<Funcion> funciones = funcionService.listarFuncionesPorEventos(eventoId);
        return ResponseEntity.ok(funciones);
    }

    @PostMapping("/eventos")
    public ResponseEntity<Evento> crearEvento(@RequestBody Evento evento) {
        Evento nuevoEvento = eventoService.guardarEvento(evento);
        return ResponseEntity.status(201).body(nuevoEvento);
    }

    private Evento conProximaFuncion(Evento evento) {
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Santiago"));
        funcionService.listarFuncionesPorEventos(evento.getId()).stream()
                .filter(f -> f.getFechaHora() != null && f.getFechaHora().isAfter(ahora))
                .min(Comparator.comparing(Funcion::getFechaHora))
                .ifPresent(f -> {
                    evento.setProximaFuncion(f.getFechaHora());
                    evento.setProximaSala(f.getSala());
                });
        return evento;
    }
}
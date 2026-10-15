package com.ticketfilms.mscartelera.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cartelera")
public class CarteleraController {

    private final EventoService eventoService;
    private final FuncionService funcionService;

    public CarteleraController(EventoService eventoService, FuncionService funcionService) {
        this.eventoService = eventoService;
        this.funcionService = funcionService;
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<Evento>> listarEventos(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String tipoEvento,
            @RequestParam(required = false) String sede,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde) {
        
        List<Evento> eventos = eventoService.listarEventosFiltrados(ciudad, region, tipoEvento, sede, genero, fechaDesde);
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
    public ResponseEntity<List<Funcion>> listarFuncionesPorEvento(
            @RequestParam Long eventoId,
            @RequestParam(required = false) LocalDateTime desde) {
        
        List<Funcion> funciones = funcionService.listarFuncionesPorEvento(eventoId, desde);
        return ResponseEntity.ok(funciones);
    }

    @PostMapping("/eventos")
    public ResponseEntity<Evento> crearEvento(@Valid @RequestBody Evento evento) {
        Evento nuevoEvento = eventoService.guardarEvento(evento);
        return ResponseEntity.status(201).body(nuevoEvento);
    }

    @GetMapping("/regiones")
    public ResponseEntity<List<String>> listarRegiones() {
        List<String> regiones = eventoService.listarRegiones();
        return ResponseEntity.ok(regiones);
    }

    @GetMapping("/ciudades")
    public ResponseEntity<List<String>> listarCiudades(@RequestParam String region) {
        List<String> ciudades = eventoService.listarCiudadesPorRegion(region);
        return ResponseEntity.ok(ciudades);
    }

    @GetMapping("/sedes")
    public ResponseEntity<List<String>> listarSedes(@RequestParam String ciudad) {
        List<String> sedes = eventoService.listarSedesPorCiudad(ciudad);
        return ResponseEntity.ok(sedes);
    }

    private Evento conProximaFuncion(Evento evento) {
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Santiago"));
        funcionService.listarFuncionesPorEvento(evento.getId(), null).stream()
                .filter(f -> f.getFechaHora() != null && f.getFechaHora().isAfter(ahora))
                .min(Comparator.comparing(Funcion::getFechaHora))
                .ifPresent(f -> {
                    evento.setProximaFuncion(f.getFechaHora());
                    evento.setProximaSala(f.getSala());
                });
        return evento;
    }
}
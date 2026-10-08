package com.ticketfilms.mscartelera.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketfilms.mscartelera.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>{

    List<Evento> findByTipoEvento(String tipoEvento);

    List<Evento> findByEstado(String estado);

    List<Evento> findByCiudad(String ciudad);

    List<Evento> findByRegion(String region);
}
package com.ticketfilms.mscartelera.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketfilms.mscartelera.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>{

    List<Evento> findByTipoEvento(String tipoEvento);

    List<Evento> findByEstado(String estado);

    List<Evento> findByCiudad(String ciudad);

    List<Evento> findByRegion(String region);

    List<Evento> findBySede(String sede);

    @Query("SELECT DISTINCT e FROM Evento e JOIN Funcion f ON f.evento.id = e.id WHERE f.fechaHora > :fechaDesde")
    List<Evento> findEventosConFuncionesPosteriores(@Param("fechaDesde") LocalDateTime fechaDesde);
}
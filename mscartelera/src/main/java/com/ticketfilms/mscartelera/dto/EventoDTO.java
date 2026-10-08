package com.ticketfilms.mscartelera.dto;

import lombok.Data;

@Data
public class EventoDTO {
    private Long id;
    private String eventoid;
    private String titulo;
    private String descripcion;
    private String tipoEvento;
    private String urlImagen;
    private Integer duracionMinutos;
    private String clasificacion;
    private String director;
    private String estado;
    private String genero;
    private String reparto;
    private String idioma;
    private String origen;
    private String ciudad;
    private String region;
    private String sede;
}

package com.ticketfilms.mscartelera.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name= "evento")
@Data
public class Evento{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "evento_id", nullable = false, unique = true)
    private String eventoid;

    @Column(name= "titulo", nullable = false)
    private String titulo;

    @Column(name= "descripcion", nullable = false)
    private String descripcion;

    @Column(name= "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(name= "url_imagen", nullable = false)
    private String urlImagen;

    @Column(name= "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(name= "clasificacion", nullable = false)
    private String clasificacion;

    @Column(name= "director", nullable = false)
    private String director;

    @Column(name= "estado", nullable = false)
    private String estado;

    @Column(name= "genero")
    private String genero;

    @Column(name= "reparto", length = 500)
    private String reparto;

    @Column(name= "idioma", length = 100)
    private String idioma;

    @Column(name= "origen", length = 100)
    private String origen;

    @Column(name= "ciudad", length = 100)
    private String ciudad;

    @Column(name= "region", length = 100)
    private String region;

    @Column(name= "sede", length = 150)
    private String sede;

    @Transient
    private LocalDateTime proximaFuncion;

    @Transient
    private String proximaSala;
}

package com.debuggerme.fiverr.sportsclubscrape;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String actividad;

    private String nombre;

    private String direccion;

    private String cp;

    private String localidad;

    private String telefono;

    private String web;
}

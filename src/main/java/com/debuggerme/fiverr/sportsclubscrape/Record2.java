package com.debuggerme.fiverr.sportsclubscrape;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Record2 {

    @Id
    private String regNo;

    private String nombre;

    private String direccion;

    private String provincia;

    private String municipio;

    private String postal;

    private String telefono;

    private String email;

    private String modalidad1;

    private String modalidad2;

    private String modalidad3;

    private String url;
}

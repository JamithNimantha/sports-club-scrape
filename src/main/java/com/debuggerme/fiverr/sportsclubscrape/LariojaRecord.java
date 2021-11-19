package com.debuggerme.fiverr.sportsclubscrape;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class LariojaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String regNo;

    private String entityName;

    private String address;

    private String postalCode;

    private String population;

    private String telephone;

    private String fax;

    private String email;

    private String sports;

    private String url;


}

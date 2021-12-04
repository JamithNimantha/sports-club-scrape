package com.debuggerme.fiverr.sportsclubscrape.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ToString
@Getter
@Setter
public class JuntaDeAndaluciaRecord {

    @Id
    private String regNo;

    private String denomination;

    private String municipality;

    private String regDate;

    private String address;

    private String province;

    private String city;

    private String zipCode;

    private String telephone;

    private String fax;

    private String email;

    private String entity;

    private String sport;

    private String dateStatuteModification;


    @Column(length = 200000)
    private String otherSports;

}

package com.hridoykrisnadas.leadstodatabase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String straat;
    private String huisnummer;
    private String toevoeging;
    private String postcode;
    private String gemeente;
    private String woonplaats;
    private String provincie;
    private String latitude;
    private String longitude;
    private String created_at;
    private String updated_at;

    public Address(String straat, String huisnummer, String toevoeging, String postcode, String gemeente, String woonplaats, String provincie, String latitude, String longitude, String created_at, String updated_at) {
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.toevoeging = toevoeging;
        this.postcode = postcode;
        this.gemeente = gemeente;
        this.woonplaats = woonplaats;
        this.provincie = provincie;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}

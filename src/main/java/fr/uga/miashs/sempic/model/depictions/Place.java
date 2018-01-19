/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model.depictions;

import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.Picture;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author niid
 */
@Entity
public class Place implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //somehow auto doesn't work
    private Long id;
    
    
    @NotNull
    private String country;
    
    @NotNull
    private String city;
    
    @NotNull
    private String region;
    
    @NotNull
    private String placeType;
    
    @NotNull
    @ManyToOne
    private Picture depictedIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public Picture getDepictedIn() {
        return depictedIn;
    }

    public void setDepictedIn(Picture depictedIn) {
        this.depictedIn = depictedIn;
    }
    
    
}

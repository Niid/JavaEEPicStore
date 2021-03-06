/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model.depictions;

import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.Picture;
import java.io.Serializable;
import javax.persistence.CascadeType;
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
public class Animal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //somehow auto doesn't work
    private Long id;
    
    
    @NotNull
    private String specie;
    
    @NotNull
    @ManyToOne(cascade=CascadeType.REMOVE)
    private Picture depictedIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public Picture getDepictedIn() {
        return depictedIn;
    }

    public void setDepictedIn(Picture depictedIn) {
        this.depictedIn = depictedIn;
    }
}

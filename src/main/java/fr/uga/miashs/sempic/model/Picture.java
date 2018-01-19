/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model;

import fr.uga.miashs.sempic.model.depictions.Animal;
import fr.uga.miashs.sempic.model.depictions.Human;
import fr.uga.miashs.sempic.model.depictions.Place;
import fr.uga.miashs.sempic.model.depictions.PicObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author niid
 */
@Entity
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Somehow auto doesn't work
    private Long id;
    
    
    //@Column(unique=true)  //same as Album... cool but ...
    @NotNull
    private String name;
    
    @NotNull
    @ManyToOne
    private Album album;
    
    @OneToMany(mappedBy="depictedIn",cascade=CascadeType.REMOVE)
    Collection<Animal> animals;
    
    @OneToMany(mappedBy="depictedIn",cascade=CascadeType.REMOVE)
    Collection<Human> humans;
    
    @OneToMany(mappedBy="depictedIn",cascade=CascadeType.REMOVE)
    Collection<PicObject> picObjects;
    
    @OneToMany(mappedBy="depictedIn",cascade=CascadeType.REMOVE)
    Collection<Place> places;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Animal> getAnimals() {
        return animals;
    }

    public Collection<Human> getHumans() {
        return humans;
    }

    public Collection<PicObject> getPicObjects() {
        return picObjects;
    }

    public Collection<Place> getPlaces() {
        return places;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Picture)) {
            return false;
        }
        Picture other = (Picture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.uga.miashs.sempic.model.Picture[ id=" + id + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author niid
 */
@Entity
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //somehow auto doesn't work
    private Long id;
    
    //@Column(unique=true) //unique was cool but meant even another user can't create a folder with the same name
    @NotNull
    private String name;
    
    @NotNull
    @ManyToOne
    private SempicUser creator;
    
    @OneToMany(mappedBy="album")
    Collection<Picture> pictures;

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public SempicUser getCreator() {
        return creator;
    }

    public void setCreator(SempicUser creator) {
        this.creator = creator;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Album)) {
            return false;
        }
        Album other = (Album) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.uga.miashs.sempic.model.Album[ id=" + id + " ]";
    }
    
}

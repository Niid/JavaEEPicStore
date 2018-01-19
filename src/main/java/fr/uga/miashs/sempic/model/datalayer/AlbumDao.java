/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model.datalayer;

import fr.uga.miashs.sempic.model.Album;
import fr.uga.miashs.sempic.model.SempicUser;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author niid
 */
@Stateless
public class AlbumDao  extends AbstractJpaDao<Album,Long> {
    
    @PersistenceContext(unitName = "SempicPU")
    private EntityManager em;
    
    public AlbumDao() {
        super(Album.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    //GET ALL ALBUMS FROM USER
    public List<Album> getByUser(SempicUser user) {
        try {
            return (List<Album>) 
                getEntityManager().createQuery("SELECT a FROM Album a "
                                                + "WHERE a.creator=:user")
                .setParameter("user", user)
                .getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}

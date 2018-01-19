/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model.datalayer;

import fr.uga.miashs.sempic.model.Album;
import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.SempicUser;
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
public class PictureDao extends AbstractJpaDao<Picture,Long> {
    
    @PersistenceContext(unitName = "SempicPU")
    private EntityManager em;
    
    
    public PictureDao() {
        super(Picture.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    //GET PICTURES FROM ALBUM
    public List<Picture> getByAlbum(Album toOpen) {
        try {
            return (List<Picture>) 
                getEntityManager().createQuery("SELECT a FROM Picture a "
                                                + "WHERE a.album=:alb")
                .setParameter("alb", toOpen)
                .getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }
    
    
}

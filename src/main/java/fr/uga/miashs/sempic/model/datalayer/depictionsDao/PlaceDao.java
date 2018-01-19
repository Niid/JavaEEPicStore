/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model.datalayer.depictionsDao;

import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.datalayer.AbstractJpaDao;
import fr.uga.miashs.sempic.model.depictions.PicObject;
import fr.uga.miashs.sempic.model.depictions.Place;
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
public class PlaceDao extends AbstractJpaDao<Place,Long> {
    
    @PersistenceContext(unitName = "SempicPU")
    private EntityManager em;


    public PlaceDao() {
        super(Place.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    //GET ALL PLACES IN PICTURE
    public List<Place> getInPicture(Picture pic) {
        try {
            return (List<Place>) 
                getEntityManager().createQuery("SELECT a FROM Place a "
                                                + "WHERE a.depictedIn=:pic")
                .setParameter("pic", pic)
                .getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}

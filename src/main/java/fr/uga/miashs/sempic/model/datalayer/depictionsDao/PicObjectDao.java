/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model.datalayer.depictionsDao;

import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.datalayer.AbstractJpaDao;
import fr.uga.miashs.sempic.model.depictions.Human;
import fr.uga.miashs.sempic.model.depictions.PicObject;
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
public class PicObjectDao extends AbstractJpaDao<PicObject,Long> {
    
    @PersistenceContext(unitName = "SempicPU")
    private EntityManager em;


    public PicObjectDao() {
        super(PicObject.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    //GET ALL OBJECTS IN PICTURE
    public List<PicObject> getInPicture(Picture pic) {
        try {
            return (List<PicObject>) 
                getEntityManager().createQuery("SELECT a FROM PicObject a "
                                                + "WHERE a.depictedIn=:pic")
                .setParameter("pic", pic)
                .getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}

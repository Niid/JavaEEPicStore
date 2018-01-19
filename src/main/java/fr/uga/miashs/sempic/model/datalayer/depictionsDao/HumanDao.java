/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model.datalayer.depictionsDao;

import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.datalayer.AbstractJpaDao;
import fr.uga.miashs.sempic.model.depictions.Animal;
import fr.uga.miashs.sempic.model.depictions.Human;
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
public class HumanDao extends AbstractJpaDao<Human,Long> {
    
    @PersistenceContext(unitName = "SempicPU")
    private EntityManager em;


    public HumanDao() {
        super(Human.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    //GET ALL HUMANS IN PICTURE
    public List<Human> getInPicture(Picture pic) {
        try {
            return (List<Human>) 
                getEntityManager().createQuery("SELECT a FROM Human a "
                                                + "WHERE a.depictedIn=:pic")
                .setParameter("pic", pic)
                .getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}

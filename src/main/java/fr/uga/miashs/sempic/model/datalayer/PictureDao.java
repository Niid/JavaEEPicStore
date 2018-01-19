/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.model.datalayer;

import fr.uga.miashs.sempic.model.Album;
import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.depictions.Animal;
import fr.uga.miashs.sempic.model.depictions.Human;
import fr.uga.miashs.sempic.model.depictions.PicObject;
import fr.uga.miashs.sempic.model.depictions.Place;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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
    
    public List<Picture> getByFilter(Album toOpen,String specie,String hName, String obj, String place) {
        try {
            
            List<Picture> picFiltered = (List<Picture>) 
                getEntityManager().createQuery("SELECT a FROM Picture a "
                                                + "WHERE a.album=:alb")
                .setParameter("alb", toOpen)
                .getResultList();
            
            List<Picture> animalOk = new ArrayList<>(picFiltered);
            List<Picture> humanOk = new ArrayList<>(picFiltered);
            List<Picture> objOk = new ArrayList<>(picFiltered);
            List<Picture> placeOk = new ArrayList<>(picFiltered);
            List<Picture> res = new ArrayList<>(picFiltered);
            
            //ANIMAL FILTER
            if(specie!=null && !specie.equals("")){
                animalOk.clear();
                picFiltered.stream().forEach((pic) -> {
                    List<String> specNames = new ArrayList<>();
                    pic.getAnimals().stream().forEach((ani) -> {
                        specNames.add(ani.getSpecie());
                    });
                    if (specNames.contains(specie)) {
                        animalOk.add(pic);
                    }
                });
            }
            
            //HUMAN FILTER
            if(hName!=null && !hName.equals("")){
                humanOk.clear();
                picFiltered.stream().forEach((pic) -> {
                    List<String> humanNames = new ArrayList<>();
                    pic.getHumans().stream().forEach((hum) -> {
                        humanNames.add(hum.getName());
                    });
                    if (humanNames.contains(hName)) {
                        humanOk.add(pic);
                    }
                });
            }
            
            //OBJECT FILTER
            if(obj!=null && !obj.equals("")){
                objOk.clear();
                picFiltered.stream().forEach((pic) -> {
                    List<String> objNames = new ArrayList<>();
                    pic.getPicObjects().stream().forEach((pObj) -> {
                        objNames.add(pObj.getName());
                    });
                    if (objNames.contains(hName)) {
                        objOk.add(pic);
                    }
                });
            }
            
            //PLACE FILTER
            if(place!=null && !place.equals("")){
                placeOk.clear();
                picFiltered.stream().forEach((pic) -> {
                    List<String> placeNames = new ArrayList<>();
                    pic.getPlaces().stream().forEach((pla) -> {
                        placeNames.add(pla.getCountry());
                    });
                    if (placeNames.contains(hName)) {
                        placeOk.add(pic);
                    }
                });
            }
            
            //CREATE RES
            picFiltered.stream().filter((pic) -> (placeOk.contains(pic) && objOk.contains(pic) && humanOk.contains(pic) && animalOk.contains(pic))).forEach((pic) -> {
                res.add(pic);
            });
            
            //RETURN LIST
            return res;
        }
        catch (NoResultException e) {
            System.out.println("NOOOOOOOOO !");
            return null;
        }
    }
    
}

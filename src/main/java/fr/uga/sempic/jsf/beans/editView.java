/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.sempic.jsf.beans;

import fr.uga.miashs.sempic.model.datalayer.depictionsDao.AnimalDao;
import fr.uga.miashs.sempic.model.datalayer.depictionsDao.HumanDao;
import fr.uga.miashs.sempic.model.datalayer.depictionsDao.PicObjectDao;
import fr.uga.miashs.sempic.model.datalayer.depictionsDao.PlaceDao;
import fr.uga.miashs.sempic.model.depictions.Animal;
import fr.uga.miashs.sempic.model.depictions.Human;
import fr.uga.miashs.sempic.model.depictions.PicObject;
import fr.uga.miashs.sempic.model.depictions.Place;
import fr.uga.miashs.sempic.util.PagesAndRoles;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author niid
 */
@Named
@ApplicationScoped
public class editView implements Serializable {
    
    @Inject
    private PictureView picView;
    
    //WAY TOO MANY DAOs, 1 for each depiction type.
    @EJB
    private AnimalDao animalDao;
    @EJB
    private HumanDao humanDao;
    @EJB
    private PicObjectDao objectDao;
    @EJB
    private PlaceDao placeDao;
    
    //WAY TOO MANY OBJECTS, 1 for each depiction type.
    private Animal animal;
    private Human human;
    private PicObject objectToAdd;
    private Place place;
    
    //WAY TOO MANY LISTS , 1 for ... you get the idea.
    private List<Animal> animals;
    private List<Human> humans;
    private List<PicObject> addedObjects;
    private List<Place> places;
    
    //LOTS OF ADD FUNCTIONS, ONE FOR EACH DEPICTION TYPE. NEEDED SINCE THE DAOs ARE DIFFERENT.
    public String addAnimal(){
        try { 
            //INSERT ALBUM INTO DB
            animal.setDepictedIn(picView.getToEdit());
            animalDao.create(animal);
            
            //RESET SELECTED
            animal=null;
            
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });
            }
        }
        //REFRESH PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    public String addHuman(){
        try { 
            //INSERT ALBUM INTO DB
            human.setDepictedIn(picView.getToEdit());
            humanDao.create(human);
            
            //RESET SELECTED
            human=null;
            
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });
            }
        }
        //REFRESH PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    public String addObject(){
        try { 
            //INSERT ALBUM INTO DB
            objectToAdd.setDepictedIn(picView.getToEdit());
            objectDao.create(objectToAdd);
            
            //RESET SELECTED
            objectToAdd=null;
            
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });
            }
        }
        //REFRESH PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    public String addPlace(){
        try { 
            //INSERT ALBUM INTO DB
            place.setDepictedIn(picView.getToEdit());
            placeDao.create(place);
            
            //RESET SELECTED
            place=null;
            
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });
            }
        }
        //REFRESH PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    //LOTS OF DELETE FUNCTIONS.
    public String deleteAnimal(Animal ani){
        try {
            //DELETE FROM DB
            animalDao.delete(ani);
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });
            }
        }
        //REFRESH PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    public String deleteHuman(Human hum){
        try {
            //DELETE FROM DB
            humanDao.delete(hum);
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });
            }
        }
        //REFRESH PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    public String deleteAddedObject(PicObject obj){
        try {
            //DELETE FROM DB
            objectDao.delete(obj);
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });
            }
        }
        //REFRESH PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    public String deletePlace(Place pla){
        try {
            //DELETE FROM DB
            placeDao.delete(pla);
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });
            }
        }
        //REFRESH PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    //GET PICTURE STREAM
    public InputStream getBigImage(){
        return picView.getPicStream(picView.getToEdit().getName());
    }
    
    //GETTERS AND SETTERS
    public Animal getAnimal() {
        if(animal==null){
            animal=new Animal();
        }
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Human getHuman() {
        if(human==null){
            human=new Human();
        }
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    public Place getPlace() {
        if(place==null){
            place=new Place();
        }
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public PicObject getObjectToAdd() {
        if(objectToAdd==null){
            objectToAdd=new PicObject();
        }
        return objectToAdd;
    }

    public void setObjectToAdd(PicObject objectToAdd) {
        this.objectToAdd = objectToAdd;
    }

    public List<Animal> getAnimals() {
        return animalDao.getInPicture(picView.getToEdit());
    }

    public List<Human> getHumans() {
        return humanDao.getInPicture(picView.getToEdit());
    }

    public List<PicObject> getAddedObjects() {
        return objectDao.getInPicture(picView.getToEdit());
    }

    public List<Place> getPlaces() {
        return placeDao.getInPicture(picView.getToEdit());
    }
    
    
}

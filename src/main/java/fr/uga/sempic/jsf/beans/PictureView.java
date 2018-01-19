/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.sempic.jsf.beans;

import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.datalayer.PictureDao;
import fr.uga.miashs.sempic.model.datalayer.depictionsDao.AnimalDao;
import fr.uga.miashs.sempic.model.datalayer.depictionsDao.HumanDao;
import fr.uga.miashs.sempic.model.datalayer.depictionsDao.PicObjectDao;
import fr.uga.miashs.sempic.model.datalayer.depictionsDao.PlaceDao;
import fr.uga.miashs.sempic.model.depictions.Animal;
import fr.uga.miashs.sempic.model.depictions.Human;
import fr.uga.miashs.sempic.model.depictions.PicObject;
import fr.uga.miashs.sempic.model.depictions.Place;
import fr.uga.miashs.sempic.util.PagesAndRoles;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author niid
 */
@Named
@ApplicationScoped
public class PictureView implements Serializable {
    
    @Inject
    private AlbumView albView;
    
    @EJB
    private PictureDao dao;
    @EJB
    private AnimalDao aDao;
    @EJB
    private HumanDao hDao;
    @EJB
    private PlaceDao pDao;
    @EJB
    private PicObjectDao poDao;
    
    private Picture selected;
    
    private Picture toEdit;
    
    private Part picturePart;   //PART RECEIVED FROM INPUTFILE

    private List<Picture> pictureList;  //LIST OF ALL PICTURES TO DISPLAY
    private List<Picture> pictureListFiltered;  //LIST OF PICTURES TO DISPLAY
    
    private boolean filtered;
    
    //FILTERS
    private String humanName;
    private String animalSpecie;
    private String objectName;
    private String placeCountry;
    
    //CREATE PICTURE
    public String create() {
        try { 
            //GET STREAM FROM INPUTFILE
            InputStream stream = picturePart.getInputStream();
            
            //INSTANCIATE AND ADD PICTURE TO DB
            this.getSelected().setName(picturePart.getSubmittedFileName());
            this.getSelected().setAlbum(albView.getToOpen());
            dao.create(selected);
            
            //STORE PICTURE IN PICSTORE
            albView.getToOpenStore().storePicture(Paths.get(selected.getName()), stream);
            
            //RESET SELECTED
            selected=null;
            
            //REFRESH PAGE
            return PagesAndRoles.pictures.path + "?faces-redirect=true";
        } catch (EJBException ex) {
            System.out.println("EJBException");
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException vEx = (ConstraintViolationException) ex.getCause();
                vEx.getConstraintViolations().forEach(cv -> {
                    System.out.println(cv);
                });
                vEx.getConstraintViolations().forEach(cv -> {
                    FacesContext.getCurrentInstance().addMessage("validationError", new FacesMessage(cv.getMessage()));
                });

            }
        } catch (IOException ex) {
            System.out.println("IOEX");
            Logger.getLogger(PictureView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    //SET PICTURE TO EDIT AND REDIRECT TO RIGHT PAGE
    public String edit(Picture pic){
        //SET PICTURE TO EDIT
        this.setToEdit(pic);
        
        //REDIRECT TO EDIT PAGE
        return PagesAndRoles.editPic.path + "?faces-redirect=true";
    }
    
    //SEARCH AND SHOW ONLY FILTERED IMAGES
    public String search(){
        //UPDATE BOOLEAN FILTERED
        filtered=true;
        
        //FILTER RESULTS
        this.pictureListFiltered=dao.getByFilter(albView.getToOpen(), animalSpecie, humanName, objectName, humanName);
        
        //REFRESH PAGE
        return PagesAndRoles.pictures.path + "?faces-redirect=true";
    }
    
    //CLEAR FILTERS AND REFRESH
    public String clearSearch(){
        //UPDATE BOOLEAN FILTERED
        filtered=false;
        
        //EMPTY FILTERS
        this.animalSpecie=null;
        this.humanName=null;
        this.objectName=null;
        this.placeCountry=null;
        this.pictureListFiltered=null;
        
        //REFRESH PAGE
        return PagesAndRoles.pictures.path + "?faces-redirect=true";
    }
    
    //GET PICTURE PATH
    public String getPic(String name){
        return this.albView.getToOpenStore().getPictureStore().toString()+"/"+name;
    }
    
    //GET PICUTRE STREAM FROM PICSTORE
    public InputStream getPicStream(String name){
        try {
            return this.albView.getToOpenStore().getPictureStream(Paths.get(name));
        } catch (IOException ex) {
            Logger.getLogger(PictureView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //REDIRECT USER -- necessary, to reset some values and avoid misplaced images.
    public String goToAlbums(){
        //RESET VALUES
        albView.setToOpen(null);
        albView.setToOpenStore(null);
        
        //REDIRECT USER
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return PagesAndRoles.albums.path + "?faces-redirect=true";
    }
    
    //DELETE PICTURE FROM DB AND PICSTORE
    public String delete(Picture pic){
        //DELETE FROM PICSTORE
        try {
            albView.getToOpenStore().deletePicture(Paths.get(pic.getName()));
        } catch (IOException ex) {
            Logger.getLogger(PictureView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //DELETE ALL DEPICTIONS
        Collection<Animal> animals =pic.getAnimals();
        animals.stream().forEach((a) -> {
            aDao.delete(a);
        });
        
        Collection<Human> humans =pic.getHumans();
        humans.stream().forEach((h) -> {
            hDao.delete(h);
        });
        
        Collection<PicObject> objs =pic.getPicObjects();
        objs.stream().forEach((p) -> {
            poDao.delete(p);
        });
        
        Collection<Place> places =pic.getPlaces();
        places.stream().forEach((p) -> {
            pDao.delete(p);
        });
        
        //DELETE FROM DB
        dao.delete(pic);
        
        //REFRESH PAGE (not necessary ?)
        return PagesAndRoles.pictures.path + "?faces-redirect=true";
    }
    
    
    
    //GETTERS AND SETTERS
    public Part getPicturePart() {
        return picturePart;
    }

    public void setPicturePart(Part picturePart) {
        this.picturePart = picturePart;
    }
    
    public Picture getSelected() {
        if (selected == null) {
            selected = new Picture();
        }
        return selected;
    }

    public void setSelected(Picture selected) {
        this.selected = selected;
    }

    public List<Picture> getPictureList() {
        this.pictureList=dao.getByAlbum(albView.getToOpen());
        return pictureList;
    }

    public void setPictureList(List<Picture> pictureList) {
        this.pictureList = pictureList;
    }

    public Picture getToEdit() {
        return toEdit;
    }

    public void setToEdit(Picture toEdit) {
        this.toEdit = toEdit;
    }

    public String getHumanName() {
        /*if(humanName==null){
            humanName="";
        }*/
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public String getAnimalSpecie() {
        /*if(animalSpecie==null){
            animalSpecie="";
        }*/
        return animalSpecie;
    }

    public void setAnimalSpecie(String animalSpecie) {
        this.animalSpecie = animalSpecie;
    }

    public String getObjectName() {
        /*if(objectName==null){
            objectName="";
        }*/
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getPlaceCountry() {
        /*if(placeCountry==null){
            placeCountry="";
        }*/
        return placeCountry;
    }

    public void setPlaceCountry(String placeCountry) {
        this.placeCountry = placeCountry;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public List<Picture> getPictureListFiltered() {
        /*if(pictureListFiltered==null){
            pictureListFiltered=new ArrayList<>();
        }*/
        return pictureListFiltered;
    }
    
    
}

    
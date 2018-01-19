/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.sempic.jsf.beans;

import fr.uga.miashs.sempic.model.Album;
import fr.uga.miashs.sempic.model.Picture;
import fr.uga.miashs.sempic.model.datalayer.AlbumDao;
import fr.uga.miashs.sempic.model.datalayer.PictureDao;
import fr.uga.miashs.sempic.model.datalayer.PictureStore;
import fr.uga.miashs.sempic.util.PagesAndRoles;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
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
    private AuthManager auth;
    
    @Inject
    private AlbumView albView;
    
    @EJB
    private PictureDao dao;
    
    private Picture selected;
    
    private Picture toEdit;
    
    private Part picturePart;   //PART RECEIVED FROM INPUTFILE

    private List<Picture> pictureList;  //LIST OF ALL PICTURES TO DISPLAY
    
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
            
            //REDIRECT USER
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
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
        
        //DELETE FROM DB
        dao.delete(pic);
        
        //REFRESH PAGE (not necessary ?)
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
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
    
    


}

    
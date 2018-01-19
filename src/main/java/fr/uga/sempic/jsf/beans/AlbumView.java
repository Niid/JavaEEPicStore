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
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author niid
 */
@Named
@SessionScoped
public class AlbumView implements Serializable {
    @Inject
    private AuthManager auth;
    
    @EJB
    private AlbumDao dao;
    @EJB
    private PictureDao picDao;
    
    private Album selected;
    private Album toOpen;   //ALBUM TO OPEN ON CLICK
    private PictureStore toOpenStore;   //PICTURE STORE TO OPEN ON CLICK
    private List<Album> albumList;     //LIST OF ALL ALBUMS TO DISPLAY

    //CREATE ALBUM
    public String create() {
        try { 
            //INSERT ALBUM INTO DB
            dao.create(selected);
            
            //RESET SELECTED
            selected=null;
            
            //REFRESH PAGE
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            return PagesAndRoles.albums.path + "?faces-redirect=true";
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
        return "";
    }

    //OPEN ALBUM
    public String openAlbum(Album albumToOpen){
        this.setToOpen(albumToOpen);
        
        //SET PICTURE STORE ON ALBUM TO OPEN
        java.nio.file.Path picPaths = Paths.get("picturesJEE/" + auth.getConnectedUser().getId(), "/"+toOpen.getName()+"/");
        java.nio.file.Path thumbPaths = Paths.get("thumbsJEE/" + auth.getConnectedUser().getId(), "/"+toOpen.getName()+"/");
        try {
            this.setToOpenStore(new PictureStore(picPaths,thumbPaths));
        } catch (IOException ex) {
            Logger.getLogger(AlbumView.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR ON PICTURESTORE CREATION");
        }
        
        //REDIRECT USER TO PICTURES PAGE
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return PagesAndRoles.pictures.path + "?faces-redirect=true";
    }
    
    //DELETE ALBUM FROM DB
    public String deleteAlbum(Album albumToDelete){
        List<Picture> picsToDelete = picDao.getByAlbum(albumToDelete);
        //DELETE PICS FILES and DB PICS ENTRIES
        java.nio.file.Path picPaths = Paths.get("picturesJEE/" + albumToDelete.getCreator().getId(), "/"+albumToDelete.getName()+"/");
        java.nio.file.Path thumbPaths = Paths.get("thumbsJEE/" + albumToDelete.getCreator().getId(), "/"+albumToDelete.getName()+"/");
        try {
            this.setToOpenStore(new PictureStore(picPaths,thumbPaths));
        } catch (IOException ex) {
            Logger.getLogger(AlbumView.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR ON PICTURESTORE CREATION");
        }
        for (Picture p : picsToDelete){
            try {
                this.toOpenStore.deletePicture(Paths.get(p.getName()));
            } catch (IOException ex) {
                Logger.getLogger(AlbumView.class.getName()).log(Level.SEVERE, null, ex);
            }
            picDao.delete(p);
        }
        //DELETE ALBUM
        dao.delete(albumToDelete);
        
        //RELOAD PAGE
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return PagesAndRoles.albums.path + "?faces-redirect=true";
    }
    
    //GETTERS AND SETTERS
    public PictureStore getToOpenStore() {
        return toOpenStore;
    }

    public void setToOpenStore(PictureStore toOpenStore) {
        this.toOpenStore = toOpenStore;
    }

    public Album getToOpen() {
        return toOpen;
    }

    public void setToOpen(Album toOpen) {
        this.toOpen = toOpen;
    }

    public List<Album> getAlbumList() {
        this.albumList=getAlbums();
        return this.albumList;
    }
    
    public Album getSelected() {
        if (selected == null) {
            selected = new Album();
            //need to get the user
            selected.setCreator(auth.getConnectedUser());
        }
        return selected;
    }

    public void setSelected(Album selected) {
        this.selected = selected;
    }
    
    public List<Album> getAlbums() {
        return dao.getByUser(auth.getConnectedUser());
    }
}

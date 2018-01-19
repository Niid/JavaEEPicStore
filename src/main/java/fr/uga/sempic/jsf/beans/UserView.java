/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.sempic.jsf.beans;

import fr.uga.miashs.sempic.model.Album;
import fr.uga.miashs.sempic.model.SempicUser;
import fr.uga.miashs.sempic.model.datalayer.*;
import fr.uga.miashs.sempic.util.PagesAndRoles;
import fr.uga.miashs.sempic.util.Roles;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 * modified by Niid
 */
@Named
@SessionScoped
public class UserView implements Serializable {

    private SempicUser selected;
    
    @Inject
    private AuthManager auth;
    
    @Inject
    private AlbumView albView;

    @EJB
    private SempicUserDao dao;
    
    @EJB
    private AlbumDao albDao;

    //CREATE USER
    public String create() {
        try { 
            selected=this.getSelected();
            //ADMIN
            if(selected.getEmail().equals("root@superuser.su")){
                Set<Roles> roles=new HashSet();
                roles.add(Roles.ADMIN);
                roles.add(Roles.USER);
                selected.setRoles(roles);
            }
            //ADD TO DATABASE
            dao.create(selected);
            
            //RESET SELECTED
            selected=null;
            
            //REDIRECT USER
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            return PagesAndRoles.login.path + "?faces-redirect=true";
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
    
    //ADMIN RELATED FUNCTIONS
    
    //PROMOTE USER TO ADMIN
    public String promoteUser(SempicUser usr){
        //SET NEW ROLES
        Set<Roles> roles=new HashSet();
        roles.add(Roles.ADMIN);
        roles.add(Roles.USER);
        usr.setRoles(roles);
        
        //UPDATE USER
        dao.promoteUser(usr);
        
        //REDIRECT USER
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return PagesAndRoles.admin.path + "?faces-redirect=true";
    }
    
    //DELETE USER
    public String deleteUser(SempicUser usr){
        //AVOID DELETING URSELF
        if(!usr.getEmail().equals(auth.getConnectedUser().getEmail())){
            //DELETE USR ALBUMS AND PICS
            List<Album> albList=albDao.getByUser(usr);
            for(Album alb : albList){
                albView.deleteAlbum(alb);
            }

            //DELETE USER
            dao.delete(usr);
        }
        //REDIRECT USER
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return PagesAndRoles.admin.path + "?faces-redirect=true";
    }

    //GETTERS AND SETTERS
    public SempicUser getSelected() {
        if (selected == null) {
            selected = new SempicUser();
            selected.setRoles(Collections.singleton(Roles.USER));
        }
        return selected;
    }

    public void setSelected(SempicUser selected) {
        this.selected = selected;
    }
    
    public List<SempicUser> getUsers() {
        return dao.readAll();
    }
}





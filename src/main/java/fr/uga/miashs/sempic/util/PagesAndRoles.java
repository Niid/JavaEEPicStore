/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.util;

import fr.uga.miashs.sempic.model.SempicUser;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 * modified (just slightly...) by Niid
 */
public enum PagesAndRoles {
    
    /*
     * Pages that are managed by the filter
     * If a page is not listed here (or listed but with no roles), then it is accesible by anyone
     * If a page is listed here with roles, then the user must have at least one of these roles to acess the page
     */
    login("/login.xhtml" ),
    createUser("/register.xhtml"),
    test("/test.xhtml", Roles.USER),
    albums("/albums.xhtml", Roles.USER),
    pictures("/pictures.xhtml",Roles.USER),
    admin("/admin.xhtml",Roles.ADMIN),
    index("/index.xhtml",Roles.ADMIN),
    editPic("/editPic.xhtml",Roles.USER);
    
    
    
    public String path;
    public Roles[] allowedRoles;

    PagesAndRoles(String page) {
        this.path=page;
        this.allowedRoles=null;
    }
    
    PagesAndRoles(String page, Roles... allowedRoles) {
        this.path=page;
        this.allowedRoles=allowedRoles;
    }
    
    private static Map<String,PagesAndRoles> idx;
    static {
        idx = new HashMap<>();
        for (PagesAndRoles p : PagesAndRoles.values()) {
            idx.put(p.path, p);
        }
    }
    public static PagesAndRoles fromPath(String path) {
        PagesAndRoles res =  idx.get(path);
        if (res==null) {
            throw new IllegalArgumentException(path+" is not in ");
        }
        return res;
    }
}

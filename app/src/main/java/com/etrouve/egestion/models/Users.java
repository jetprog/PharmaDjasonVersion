package com.etrouve.egestion.models;

import java.util.Date;

/**
 * Created by ingdjason on 6/20/17.
 */

public class Users {

    private String telephone;
    private String type;
    private String password;
    private String name;
    private String email;
    private Entreprise_info entrepriseID;
    private String objectId;
    private String userStatus;
    private Date created;
    private Date lastLogin;

    public Users(){

    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Entreprise_info getEntrepriseID() {
        return entrepriseID;
    }

    public void setEntrepriseID(Entreprise_info entrepriseID) {
        this.entrepriseID = entrepriseID;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}

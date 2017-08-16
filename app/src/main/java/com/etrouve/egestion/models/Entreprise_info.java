package com.etrouve.egestion.models;

import java.util.Date;

/**
 * Created by ingdjason on 6/20/17.
 */

public class Entreprise_info {

    private String tel_entreprise;
    private String nom_entreprise;
    private String email_entreprise;
    private String adresse_entreprise;
    private Zone_Ville zoneID;
    private String objectId;
    private Date created;

    public Entreprise_info() {
    }

    public String getTel_entreprise() {
        return tel_entreprise;
    }

    public void setTel_entreprise(String tel_entreprise) {
        this.tel_entreprise = tel_entreprise;
    }

    public String getNom_entreprise() {
        return nom_entreprise;
    }

    public void setNom_entreprise(String nom_entreprise) {
        this.nom_entreprise = nom_entreprise;
    }

    public String getEmail_entreprise() {
        return email_entreprise;
    }

    public void setEmail_entreprise(String email_entreprise) {
        this.email_entreprise = email_entreprise;
    }

    public String getAdresse_entreprise() {
        return adresse_entreprise;
    }

    public void setAdresse_entreprise(String adresse_entreprise) {
        this.adresse_entreprise = adresse_entreprise;
    }

    public Zone_Ville getZoneID() {
        return zoneID;
    }

    public void setZoneID(Zone_Ville zoneID) {
        this.zoneID = zoneID;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}

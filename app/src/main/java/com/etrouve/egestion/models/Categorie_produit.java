package com.etrouve.egestion.models;

import java.util.Date;

/**
 * Created by ingdjason on 6/20/17.
 */

public class Categorie_produit {

    private String nom_categorie;
    private String desc_categorie;
    private Entreprise_info entrepriseID;
    private String objectId;
    private Date created;

    public Categorie_produit(){

    }

    public String getNom_categorie() {
        return nom_categorie;
    }

    public void setNom_categorie(String nom_categorie) {
        this.nom_categorie = nom_categorie;
    }

    public String getDesc_categorie() {
        return desc_categorie;
    }

    public void setDesc_categorie(String desc_categorie) {
        this.desc_categorie = desc_categorie;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}


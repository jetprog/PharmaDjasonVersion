package com.etrouve.egestion.models;

import java.util.Date;

/**
 * Created by ingdjason on 6/20/17.
 */

public class UniteRef_entreprise {
    private String nom_uniteRef;
    private String desc_uniteRef;
    public Entreprise_info entrepriseID;
    private String objectId;
    private Date created;

    public UniteRef_entreprise() {
    }

    public String getNom_uniteRef() {
        return nom_uniteRef;
    }

    public void setNom_uniteRef(String nom_uniteRef) {
        this.nom_uniteRef = nom_uniteRef;
    }

    public String getDesc_uniteRef() {
        return desc_uniteRef;
    }

    public void setDesc_uniteRef(String desc_uniteRef) {
        this.desc_uniteRef = desc_uniteRef;
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

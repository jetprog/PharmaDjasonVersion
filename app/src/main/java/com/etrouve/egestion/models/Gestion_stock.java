package com.etrouve.egestion.models;

import java.util.Date;

/**
 * Created by ingdjason on 6/20/17.
 */

public class Gestion_stock {

    private  Double montantTotal_stock;
    private String id_stock;
    private Date date_stock;
    private String description_stock;
    private Entreprise_info entrepriseID;
    private String objectId;
    private Date created;

    public Gestion_stock() {
    }

    public Double getMontantTotal_stock() {
        return montantTotal_stock;
    }

    public void setMontantTotal_stock(Double montantTotal_stock) {
        this.montantTotal_stock = montantTotal_stock;
    }

    public String getId_stock() {
        return id_stock;
    }

    public void setId_stock(String id_stock) {
        this.id_stock = id_stock;
    }

    public Date getDate_stock() {
        return date_stock;
    }

    public void setDate_stock(Date date_stock) {
        this.date_stock = date_stock;
    }

    public String getDescription_stock() {
        return description_stock;
    }

    public void setDescription_stock(String description_stock) {
        this.description_stock = description_stock;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Entreprise_info getEntrepriseID() {
        return entrepriseID;
    }

    public void setEntrepriseID(Entreprise_info entrepriseID) {
        this.entrepriseID = entrepriseID;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}

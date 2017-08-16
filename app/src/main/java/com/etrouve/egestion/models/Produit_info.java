package com.etrouve.egestion.models;

import java.util.Date;

/**
 * Created by ingdjason on 6/20/17.
 */

public class Produit_info {

    private Gestion_stock gestionStockID;
    private Categorie_produit categorieID;
    private Double prixAch_produit;
    private int qte_produit;
    private String nom_produit;
    private Date expiration_produit;
    private int limit_produit;
    private String objectId;
    private Date created;

    public Produit_info() {
    }

    public Gestion_stock getGestionStockID() {
        return gestionStockID;
    }

    public void setGestionStockID(Gestion_stock gestionStockID) {
        this.gestionStockID = gestionStockID;
    }

    public Categorie_produit getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(Categorie_produit categorieID) {
        this.categorieID = categorieID;
    }

    public Double getPrixAch_produit() {
        return prixAch_produit;
    }

    public void setPrixAch_produit(Double prixAch_produit) {
        this.prixAch_produit = prixAch_produit;
    }

    public int getQte_produit() {
        return qte_produit;
    }

    public void setQte_produit(int qte_produit) {
        this.qte_produit = qte_produit;
    }

    public String getNom_produit() {
        return nom_produit;
    }

    public void setNom_produit(String nom_produit) {
        this.nom_produit = nom_produit;
    }

    public Date getExpiration_produit() {
        return expiration_produit;
    }

    public void setExpiration_produit(Date expiration_produit) {
        this.expiration_produit = expiration_produit;
    }

    public int getLimit_produit() {
        return limit_produit;
    }

    public void setLimit_produit(int limit_produit) {
        this.limit_produit = limit_produit;
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

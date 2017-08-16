package com.etrouve.egestion.models;

import java.util.Date;

/**
 * Created by ingdjason on 6/20/17.
 */

public class UniteRef_produit {

    private UniteRef_entreprise uniteEnt_ID;
    private Produit_info produitID;
    private Double prix_unite;
    private Double promo_unite;
    private Date created;
    private Date updated;
    private String objectId;

    public UniteRef_produit() {
    }

    public UniteRef_entreprise getUniteEnt_ID() {
        return uniteEnt_ID;
    }

    public void setUniteEnt_ID(UniteRef_entreprise uniteEnt_ID) {
        this.uniteEnt_ID = uniteEnt_ID;
    }

    public Produit_info getProduitID() {
        return produitID;
    }

    public void setProduitID(Produit_info produitID) {
        this.produitID = produitID;
    }

    public Double getPrix_unite() {
        return prix_unite;
    }

    public void setPrix_unite(Double prix_unite) {
        this.prix_unite = prix_unite;
    }

    public Double getPromo_unite() {
        return promo_unite;
    }

    public void setPromo_unite(Double promo_unite) {
        this.promo_unite = promo_unite;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}

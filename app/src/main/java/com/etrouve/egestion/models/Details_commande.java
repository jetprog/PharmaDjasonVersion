package com.etrouve.egestion.models;

/**
 * Created by ingdjason on 6/21/17.
 */

public class Details_commande {

    private UniteRef_produit uniteRefProdID;
    private Users usersID;
    private Produit_info produitID;
    private Double qteVendu;
    private int  idVente;

    public Details_commande() {
    }

    public UniteRef_produit getUniteRefProdID() {
        return uniteRefProdID;
    }

    public void setUniteRefProdID(UniteRef_produit uniteRefProdID) {
        this.uniteRefProdID = uniteRefProdID;
    }

    public Users getUsersID() {
        return usersID;
    }

    public void setUsersID(Users usersID) {
        this.usersID = usersID;
    }

    public Produit_info getProduitID() {
        return produitID;
    }

    public void setProduitID(Produit_info produitID) {
        this.produitID = produitID;
    }

    public Double getQteVendu() {
        return qteVendu;
    }

    public void setQteVendu(Double qteVendu) {
        this.qteVendu = qteVendu;
    }

    public int getIdVente() {
        return idVente;
    }

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }
}

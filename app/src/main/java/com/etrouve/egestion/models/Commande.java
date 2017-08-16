package com.etrouve.egestion.models;

import java.util.Date;

/**
 * Created by ingdjason on 6/21/17.
 */

public class Commande {

    private Double qteTotalProduit;
    private int idCommande;
    private Double prixTotalCommande;
    private Users usersID;
    private Date created;
    private String objectId;
    private Zone_Ville zoneClientID;
    private String emailClient;
    private String adresseClient;
    private String telephoneClient;
    private String nomClient;

    public Commande() {
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Double getQteTotalProduit() {
        return qteTotalProduit;
    }

    public void setQteTotalProduit(Double qteTotalProduit) {
        this.qteTotalProduit = qteTotalProduit;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public Double getPrixTotalCommande() {
        return prixTotalCommande;
    }

    public void setPrixTotalCommande(Double prixTotalCommande) {
        this.prixTotalCommande = prixTotalCommande;
    }

    public Users getUsersID() {
        return usersID;
    }

    public void setUsersID(Users usersID) {
        this.usersID = usersID;
    }

    public Zone_Ville getZoneClientID() {
        return zoneClientID;
    }

    public void setZoneClientID(Zone_Ville zoneClientID) {
        this.zoneClientID = zoneClientID;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
}

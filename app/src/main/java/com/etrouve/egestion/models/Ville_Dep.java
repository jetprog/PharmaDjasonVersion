package com.etrouve.egestion.models;

/**
 * Created by ingdjason on 6/20/17.
 */

public class Ville_Dep {

    private String nom_ville;
    private String code_ville;
    private String objectId;
    private Departement departementID;

    public Ville_Dep() {
    }

    public String getNom_ville() {
        return nom_ville;
    }

    public void setNom_ville(String nom_ville) {
        this.nom_ville = nom_ville;
    }

    public String getCode_ville() {
        return code_ville;
    }

    public void setCode_ville(String code_ville) {
        this.code_ville = code_ville;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Departement getDepartementID() {
        return departementID;
    }

    public void setDepartementID(Departement departementID) {
        this.departementID = departementID;
    }
}

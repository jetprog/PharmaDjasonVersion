package com.etrouve.egestion.models;

/**
 * Created by ingdjason on 6/20/17.
 */

public class Zone_Ville {

    private Ville_Dep villeID;
    private String nom_zone;
    private String code_zone;
    private String obejectId;

    public Zone_Ville() {
    }

    public Ville_Dep getVilleID() {
        return villeID;
    }

    public void setVilleID(Ville_Dep villeID) {
        this.villeID = villeID;
    }

    public String getNom_zone() {
        return nom_zone;
    }

    public void setNom_zone(String nom_zone) {
        this.nom_zone = nom_zone;
    }

    public String getCode_zone() {
        return code_zone;
    }

    public void setCode_zone(String code_zone) {
        this.code_zone = code_zone;
    }

    public String getObejectId() {
        return obejectId;
    }

    public void setObejectId(String obejectId) {
        this.obejectId = obejectId;
    }
}

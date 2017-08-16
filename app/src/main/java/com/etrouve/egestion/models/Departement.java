package com.etrouve.egestion.models;

/**
 * Created by ingdjason on 6/20/17.
 */

public class Departement {

    private String info_dep;
    private String code_dep;
    private String objectId;

    public Departement() {
    }

    public String getInfo_dep() {
        return info_dep;
    }

    public void setInfo_dep(String info_dep) {
        this.info_dep = info_dep;
    }

    public String getCode_dep() {
        return code_dep;
    }

    public void setCode_dep(String code_dep) {
        this.code_dep = code_dep;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}

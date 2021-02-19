package com.dxc.hibernate;

@SuppressWarnings("serial")
public class CsroService implements java.io.Serializable {

    private int csruServiceId;
    private String serviceName;

    public CsroService() {
    }

    public CsroService(int csruServiceId) {
        this.csruServiceId = csruServiceId;
    }

    public CsroService(int csruServiceId, String serviceName) {
        this.csruServiceId = csruServiceId;
        this.serviceName = serviceName;
    }

    public int getCsruServiceId() {
        return this.csruServiceId;
    }

    public void setCsruServiceId(int csruServiceId) {
        this.csruServiceId = csruServiceId;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}

package com.dxc.hibernate;

@SuppressWarnings("serial")
public class ServiceProvider implements java.io.Serializable {

    private int serviceProviderId;
    private String wsdlUrl;
    private Integer csruServiceId;
    private String methodName;

    public ServiceProvider() {
    }

    public ServiceProvider(int serviceProviderId, String wsdlUrl) {
        this.serviceProviderId = serviceProviderId;
        this.wsdlUrl = wsdlUrl;
    }

    public ServiceProvider(int serviceProviderId, String wsdlUrl, Integer csruServiceId, String methodName) {
        this.serviceProviderId = serviceProviderId;
        this.wsdlUrl = wsdlUrl;
        this.csruServiceId = csruServiceId;
        this.methodName = methodName;
    }

    public int getServiceProviderId() {
        return this.serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getWsdlUrl() {
        return this.wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public Integer getCsruServiceId() {
        return this.csruServiceId;
    }

    public void setCsruServiceId(Integer csruServiceId) {
        this.csruServiceId = csruServiceId;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

}

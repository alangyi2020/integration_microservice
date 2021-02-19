package com.dxc.hibernate;

@SuppressWarnings("serial")
public class RequestParameters implements java.io.Serializable {

    private RequestParametersId id;

    public RequestParameters() {
    }

    public RequestParameters(RequestParametersId id) {
        this.id = id;
    }

    public RequestParametersId getId() {
        return this.id;
    }

    public void setId(RequestParametersId id) {
        this.id = id;
    }

}
package com.dxc.hibernate;

import java.util.Date;

public class Request {
    private static final long serialVersionUID = 1L;
    private RequestId id;
    private Date requestDatetime;
    private Date responseDatetime;
    private Integer csruServiceId;

    public Request() {
    }

    public Request(RequestId id, Date requestDatetime) {
        this.id = id;
        this.requestDatetime = requestDatetime;
    }

    public Request(RequestId id, Date requestDatetime, Date responseDatetime, Integer csruServiceId) {
        this.id = id;
        this.requestDatetime = requestDatetime;
        this.responseDatetime = responseDatetime;
        this.csruServiceId = csruServiceId;
    }

    public RequestId getId() {
        return this.id;
    }

    public void setId(RequestId id) {
        this.id = id;
    }

    public Date getRequestDatetime() {
        return this.requestDatetime;
    }

    public void setRequestDatetime(Date requestDatetime) {
        this.requestDatetime = requestDatetime;
    }

    public Date getResponseDatetime() {
        return this.responseDatetime;
    }

    public void setResponseDatetime(Date responseDatetime) {
        this.responseDatetime = responseDatetime;
    }

    public Integer getCsruServiceId() {
        return this.csruServiceId;
    }

    public void setCsruServiceId(Integer csruServiceId) {
        this.csruServiceId = csruServiceId;
    }

}

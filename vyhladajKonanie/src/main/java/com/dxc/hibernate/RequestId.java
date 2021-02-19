package com.dxc.hibernate;

public class RequestId implements java.io.Serializable {

    private int requestId;
    private int serviceProviderId;

    public RequestId() {
    }

    public RequestId(int requestId, int serviceProviderId) {
        this.requestId = requestId;
        this.serviceProviderId = serviceProviderId;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getServiceProviderId() {
        return this.serviceProviderId;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof RequestId))
            return false;
        RequestId castOther = (RequestId) other;

        return (this.getRequestId() == castOther.getRequestId())
                && (this.getServiceProviderId() == castOther.getServiceProviderId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getRequestId();
        result = 37 * result + this.getServiceProviderId();
        return result;
    }

}
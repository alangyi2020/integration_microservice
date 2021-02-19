package com.dxc.hibernate;


public class RequestParametersId implements java.io.Serializable {

    private int requestParametersId;
    private int requestId;
    private String parameterName;
    private String parameterValue;
    private int serviceProviderId;

    public RequestParametersId() {
    }

    public RequestParametersId(int requestParametersId, int requestId, String parameterName, String parameterValue,
                               int serviceProviderId) {
        this.requestParametersId = requestParametersId;
        this.requestId = requestId;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.serviceProviderId = serviceProviderId;
    }

    public int getRequestParametersId() {
        return this.requestParametersId;
    }

    public void setRequestParametersId(int requestParametersId) {
        this.requestParametersId = requestParametersId;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return this.parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
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
        if (!(other instanceof RequestParametersId))
            return false;
        RequestParametersId castOther = (RequestParametersId) other;

        return (this.getRequestParametersId() == castOther.getRequestParametersId())
                && (this.getRequestId() == castOther.getRequestId())
                && ((this.getParameterName() == castOther.getParameterName())
                || (this.getParameterName() != null && castOther.getParameterName() != null
                && this.getParameterName().equals(castOther.getParameterName())))
                && ((this.getParameterValue() == castOther.getParameterValue())
                || (this.getParameterValue() != null && castOther.getParameterValue() != null
                && this.getParameterValue().equals(castOther.getParameterValue())))
                && (this.getServiceProviderId() == castOther.getServiceProviderId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getRequestParametersId();
        result = 37 * result + this.getRequestId();
        result = 37 * result + (getParameterName() == null ? 0 : this.getParameterName().hashCode());
        result = 37 * result + (getParameterValue() == null ? 0 : this.getParameterValue().hashCode());
        result = 37 * result + this.getServiceProviderId();
        return result;
    }

}

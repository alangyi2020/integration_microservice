package com.dxc.hibernate;

public class RequestItems  {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private RequestItemsId id;

    public RequestItems() {
        System.out.println("ajkakjakjak");
    }

    public RequestItems(RequestItemsId id) {
        this.id = id;
    }

    public RequestItemsId getId() {
        return this.id;
    }

    public void setId(RequestItemsId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request_Items_id: ").append(id.getRequestItemsId()).append(" Service_Provider_id: ").append(id.getServiceProviderId()).append(" method_call_datetime: ").append(id.getMethodCallDatetime()).append(" status: ").append(id.getStatus()).append(" CSRU_Service_id ").append(id.getCsruServiceId()).append(" Request_id ").append(id.getRequestItemsId()).append("\n");
        return sb.toString();
    }

    public String toSave() {
        StringBuilder sb = new StringBuilder();
        // sb.append("Request_Items_id: ").append(id.getRequestItemsId()).append(" Service_Provider_id: ").append(id.getServiceProviderId()).append(" method_call_datetime: ").append(id.getMethodCallDatetime()).append(" status: ").append(id.getStatus()).append(" CSRU_Service_id ").append(id.getCsruServiceId()).append(" Request_id ").append(id.getRequestItemsId()).append("\n");
        sb.append("update soapdemo.Request_Items set Service_Provider_id = ").append(id.getServiceProviderId()).append(", method_call_datetime = now()").append(", status = ").append(id.getStatus()).append(", CSRU_Service_id = ").append(id.getCsruServiceId()).append(", Request_id = ").append(id.getRequestItemsId()).append(" where Request_Items_id = ").append(id.getRequestItemsId());

        return sb.toString();
    }

}
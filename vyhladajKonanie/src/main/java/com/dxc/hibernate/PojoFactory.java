package com.dxc.hibernate;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PojoFactory {
    protected static PojoFactory factory = null;
    public Session session;
    protected Transaction tx;
    protected int Request_Items_id;

    private PojoFactory() throws ClassNotFoundException, SQLException {
    	//HibernateUtil.getSessionFactory().
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
    }

    public static PojoFactory getInstance() throws SQLException {
        if (factory == null ) {
            try {
                factory = new PojoFactory();
            } catch (ClassNotFoundException cnfe) {
                throw new SQLException(cnfe.getMessage());
            }
        }
        return factory;
    }
    public void close() throws SQLException {
        session.close();
    }
    public void commit() throws SQLException {
        tx.commit();
        tx = session.beginTransaction();
    }
    public int getServiceProviderId(String methodName) {
        SQLQuery query = session.createSQLQuery("select DISTINCT sp.Service_Provider_id from soapdemo.Service_Provider sp where sp.method_name = '" + methodName + "'");
        List<Object> rows = query.list();
        Request_Items_id =  Integer.parseInt(rows.get(0).toString());
        return Request_Items_id;
    }

    public Request getRequest(Object[] row) {

        Request request = new Request();
        request.setId(new RequestId((Integer) row[0], (Integer) row[4]));
        request.setRequestDatetime((Date) row[1]);
        request.setResponseDatetime((Date) row[2]);
        request.setCsruServiceId((Integer)row[3]);
        return request;
    }

    public List<Request> getRequestsbyProvider(int id, int provider_id) throws SQLException {
        List<Request> requests = new ArrayList<Request>();

        SQLQuery query = session.createSQLQuery("SELECT distinct request_id,request_datetime, response_datetime,CSRU_Service_id,Service_Provider_id FROM soapdemo.Request where request_id = " + id + " and  Service_Provider_id = " + provider_id);

        @SuppressWarnings("unchecked")
        List<Object[]> rows =  query.list();

        for(Object[] row : rows){
            requests.add(getRequest(row));
        }

        return requests;
    }

    public List<RequestParameters> getRequest_parametersByServiceProviderId(int ServiceProviderId) {
        List<RequestParameters> req_pars = new ArrayList<RequestParameters>();
        //tx = session.beginTransaction();
        //SQLQuery query = session.byId("Request");
        SQLQuery query = session.createSQLQuery("SELECT Request_parameters_id,request_id,Parameter_name,Parameter_value,Service_Provider_id FROM soapdemo.Request_parameters where Service_Provider_id = " + ServiceProviderId);
        List<Object[]> rows =  query.list();
        for(Object[] row : rows){
            req_pars.add(getRequest_parameter(row));

        }
        return req_pars;
    }

    private RequestParameters getRequest_parameter(Object[] row) {
        RequestParameters record = new RequestParameters();
        RequestParametersId rp = new RequestParametersId();
        rp.setRequestParametersId((Integer) row[0]);
        rp.setRequestId((Integer) row[1]);
        rp.setParameterName((String) row[2]);
        rp.setParameterValue((String) row[3]);
        rp.setServiceProviderId((Integer) row[4]);
        record.setId(rp);
        return record;
    }
    public int get_max_Request_Items_id() {
        SQLQuery query = session.createSQLQuery("select coalesce(max(Request_Items_id),0) from Request_Items");
        List<Object> rows = query.list();
        Request_Items_id =  Integer.parseInt(rows.get(0).toString());
        return Request_Items_id;
    }

    public int getRequestType(int request_id) {

        SQLQuery query = session.createSQLQuery("SELECT Request_Items_id,Service_Provider_id,method_call_datetime,status,CSRU_Service_id,Request_id FROM soapdemo.Request_Items where request_id = " + request_id);

        List<Object> rows = query.list();
        if (rows.size() == 0) {
            return 0;
        } else {
            query = session.createSQLQuery("SELECT max(status)  FROM soapdemo.Request_Items where request_id = " + request_id);
            rows = query.list();
            int maxStatus =  Integer.parseInt(rows.get(0).toString());
            if (maxStatus == 0) {
                return 1;
            } else return 2;

        }
    }

    public RequestItems set_Request_Items(@org.jetbrains.annotations.NotNull Request myrow) throws SQLException {
        RequestItems req_items  = new com.dxc.hibernate.RequestItems();
        RequestItemsId ri = new RequestItemsId();
        ri.setRequestItemsId(++Request_Items_id);
        RequestId myId = myrow.getId();
        int myid = (Integer) myId.getServiceProviderId();
        ri.setServiceProviderId(myid);
        //ri.setServiceProviderId(myrow.getId().getServiceProviderId());
        ri.setStatus(-1);
        ri.setCsruServiceId(myrow.getCsruServiceId());
        //ri.setRequestId(myrow.getId().getRequestId());
        req_items.setId(ri);
        //Create DB record
        //session.save(req_items);

        sqlQuery query = new sqlQuery();
        query.update(" insert into soapdemo.Request_Items (Request_Items_id,Service_Provider_id,status,CSRU_Service_id) VALUES (" + ri.getRequestItemsId() + "," +
                ri.getServiceProviderId() + ",-1,"+ ri.getCsruServiceId() +")");



         return req_items;
    }

    public List<RequestItems> getNotProcessedRequest_Items(int request_id) {
        List<RequestItems> req_items = new ArrayList<RequestItems>();

        SQLQuery query = session.createSQLQuery("SELECT Request_Items_id,Service_Provider_id,method_call_datetime,status,CSRU_Service_id,Request_id FROM soapdemo.Request_Items where status <> 0 and request_id = " + request_id);
        List<Object[]> rows =  query.list();
        for(Object[] row : rows){
            if((Integer) row[3] < 0 ) {
                // request has set, but have not been called
                //in database is exists record
                //save not required
                req_items.add(getRequestItems(row));
            }
            if((Integer) row[3] > 0 ) {
                // request has set and have  been called
                //save is required
                req_items.add(getNewRequestItems(row));
            }

            //

        }
        return req_items;
    }

    private RequestItems getRequestItems(Object[] row) {
        RequestItems request = new RequestItems();
        RequestItemsId ri = new RequestItemsId();
        ri.setRequestItemsId((Integer) row[0]);
        ri.setServiceProviderId((Integer) row[1]);
        ri.setMethodCallDatetime((Date) row[2]);
        ri.setStatus((Integer)row[3]);
        ri.setCsruServiceId((Integer) row[4]);
        ri.setRequestId((Integer) row[5]);
        request.setId(ri);

        return request;
    }

    private RequestItems getNewRequestItems(Object[] row) {
        RequestItems request = new RequestItems();
        RequestItemsId ri = new RequestItemsId();

        ri.setRequestItemsId(++Request_Items_id);
        ri.setServiceProviderId((Integer) row[1]);
        ri.setStatus(-1);
        ri.setCsruServiceId((Integer) row[4]);
        ri.setRequestId((Integer) row[5]);
        request.setId(ri);
        session.save(request);
        return request;
    }

    public ServiceProvider getService_Provider(int Service_Provider_id) {
        ServiceProvider sp = new ServiceProvider();
        SQLQuery query = session.createSQLQuery("SELECT Service_Provider_id,wsdl_url,CSRU_Service_id,method_name FROM soapdemo.Service_Provider where  Service_Provider_id = " + Service_Provider_id);
        List<Object[]> rows =  query.list();
        for(Object[] row : rows){
            sp.setServiceProviderId((Integer) row[0]);
            sp.setWsdlUrl((String) row[1]);
            sp.setCsruServiceId((Integer) row[2]);
            sp.setMethodName((String) row[3]);

        }
        return sp;
    }
    
    public List<RequestParameters> getRequest_parameters_by_SP_ID(int Service_Provider_id, List<RequestParameters> rp) {
		List<RequestParameters> r_p =new ArrayList<RequestParameters>();
		for(RequestParameters r_pp : rp) {
			if(r_pp.getId().getServiceProviderId() == Service_Provider_id) {
				r_p.add(r_pp);
			}
		}
		
		return r_p;
	}
    
    public void updateRequest_Items(RequestItems ri) {
    	System.out.println("ri= " + ri.toString());
    	System.out.println("update = " + ri.toSave());
    	System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    	
    	sqlQuery query = new sqlQuery();
        query.update(ri.toSave());
    	
    	
    }

    public void insertIntoResponse(Response resp){
        System.out.println("insert = " + resp.toSave(resp));
        sqlQuery query = new sqlQuery();
        query.update(resp.toSave(resp));
    }




}

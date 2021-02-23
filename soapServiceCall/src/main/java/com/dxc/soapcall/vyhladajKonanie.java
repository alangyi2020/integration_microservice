package com.dxc.soapcall;

import com.dxc.hibernate.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class vyhladajKonanie {
    public String callVyhladajKonanie(int request_id) throws SQLException {
        String return_value = "";
        String outDir = "/home/dxc/java/" + request_id;

        PojoFactory factory = PojoFactory.getInstance();
        try {
            int CSRU_Service_id = factory.getServiceProviderId("vyhladajKonanie");
            int provider_id = CSRU_Service_id;

            //get request details...
            List<Request> requests = factory.getRequestsbyProvider(request_id, CSRU_Service_id);
            for (Request myrow : requests) {
                System.out.println(myrow.toString());
                CSRU_Service_id = myrow.getCsruServiceId();
            }

            //Load request_parameters by request_id
            List<RequestParameters> request_parameters = factory.getRequest_parametersByServiceProviderId(CSRU_Service_id);

            int max_Request_Items_id = factory.get_max_Request_Items_id();
            System.out.println(max_Request_Items_id);

            List<ServiceProvider> serc_prov = new ArrayList<ServiceProvider>();

            int request_type = factory.getRequestType(request_id);
            /*
             * 0 - new
             * 1 - finished
             * 2 - failed or not done
             */

            request_type = 0;
            List<RequestItems> Req_Items = new ArrayList<RequestItems>();
            System.out.println("****************************arpi****************************\n");

            if(request_type != 1) {
                if(request_type == 0) {
                    for (Request myrow : requests) {
                        System.out.println("create request_items record from reuest: " + myrow.toString());
                        RequestItems ri = factory.set_Request_Items(myrow);
                        System.out.println("Created new request_items: " + ri.toString());
                        Req_Items.add(ri);
                    }

                }
                if(request_type == 2) {
                    Req_Items = factory.getNotProcessedRequest_Items(request_id);
                    for (RequestItems ri : Req_Items) {
                        System.out.println("Prepared not finished request_items: " + ri.toString());
                    }
                }

                System.out.println("****************************Mapping****************************\n");
                int callResult = 0;

                for (RequestItems ri : Req_Items) {
                    System.out.println("Prepared not finished request_items: " + ri.toString());
                }


            }







            return_value = "" + max_Request_Items_id + " request_type = " + request_type;

        } catch (Exception e) {
            return_value =   e.getMessage() + "|3|"  + request_id + "|";

        } finally {
            //factory.close();
            factory.commit();
            factory.close();
        }

        return return_value;
    }
}

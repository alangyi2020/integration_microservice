package com.dxc.soapcall;

import com.dxc.common_tools.Write_Xml;
import com.dxc.common_tools.XMLUtil;
import com.dxc.hibernate.*;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* NOVA SLUZBA getZoznamSudov */
public class getZoznamSudov {

	public String callgetZoznamSudov(int request_id) throws SQLException {
        String return_value = "";
        String outDir = "/tmp/" + request_id;

        PojoFactory factory = PojoFactory.getInstance();
        try {
        	/*TODO 
        	 * premenovat NazovSLuzby na skutocny nazov
        	 */
            int CSRU_Service_id = factory.getServiceProviderId("getZoznamSudov");
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

            List<ServiceProvider> serc_prov = new ArrayList<ServiceProvider>(); //esponse;

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
                    ServiceProvider sp = factory.getService_Provider(ri.getId().getServiceProviderId() /*Service_Provider_id()*/);
                    serc_prov.add(sp);
                    System.out.println("Service_provider: " + sp.toString());
                    String sp_name = sp.getMethodName();
                    int call_s = 3;
                    String ResponseXml = "";
                    String myFile = "";
                    /*TODO
                     * Deklaracia objektu podla nazzvu I_ServiceTemplate a samozrejme premenovat to 
                     * I_ServiceTemplate obj_Service = null;
                     * obj_Service = new I_ServiceTemplate();
                     * obj_Service.setURL(sp.getWsdlUrl());
                     */
                    I_getZoznamSudov obj_Service = null;
                    obj_Service = new I_getZoznamSudov();
                    obj_Service.setURL(sp.getWsdlUrl());                 

                    for(RequestParameters re_pa:  factory.getRequest_parameters_by_SP_ID(sp.getServiceProviderId(), request_parameters)) {
                        System.out.println("Service_parameter = " + re_pa.toString());
                        String met_name = "set"+re_pa.getId().getParameterName();
                        System.out.println(met_name);
                        String par_value = re_pa.getId().getParameterValue();
                        System.out.println(par_value);
                        /*TODO spravne premenovat podla I_service_template
                         * Method m = <I_service_template>.class.getDeclaredMethod(met_name,String.class);
                         * m.invoke(obj_Service, par_value);                         * 
                         */
                        Method m = I_getZoznamSudov.class.getDeclaredMethod(met_name,String.class);
                        m.invoke(obj_Service, par_value);  
                        
                    }
                    ri.getId().setMethodCallDatetime(new Date());
                    ri.getId().setStatus(2);   /*setstatus(2);*/
                    System.out.println("Status set for request_items: " + ri.toString());
                    factory.updateRequest_Items(ri);

                    //factory.session.update(ri);
                    //factory.commit();
                    Date myDate = new Date();
                    /*TODO spravne premenovat!
                     * call_s =  obj_Service.callService();
                     * ResponseXml = XMLUtil.toXML(o_vyhladajKonanie.getVyhladajKonanieResponse());
                    	myFile = "VyhladajKonanieResponse_" + request_id + "_" +  myDate.getTime() + ".xml";
                     */
                    call_s =  obj_Service.callService();
                
					ResponseXml = XMLUtil.toXML( obj_Service.getGetZoznamSudovResponse());
					/*druha moznost ?? ktora je spravna r.121 alebo 123 ??
					ResponseXml = XMLUtil.toXML(obj_Service.getGetZoznamSudovResponse());
					*/
                   	myFile = "ZoznamSudovResponse_" + request_id + "_" +  myDate.getTime() + ".xml";
                    
                    
                    if( call_s== 0) {
                        ri.getId().setStatus(0);
                        System.out.println("request_items finished: " + ri.toString());
                        System.out.println("request_items update: " + ri.toSave());
                        //factory.execute_update(ri.toSave());
                        //factory.session.update(ri);
                        factory.updateRequest_Items(ri);
                        //factory.commit();
                        //factory.setRequest_ResponseTime(ri.getId().getRequestId(),ri.getId().getServiceProviderId());
                        Write_Xml write = new Write_Xml();
                        //System.out.println(myFile);
                        write.whenWriteStringUsingBufferedWritter_thenCorrect(outDir,myFile, ResponseXml);
                        //System.out.println(ResponseXml);
                    } else {
                        ri.getId().setStatus(1);

                        //factory.session.update(ri);
                        factory.updateRequest_Items(ri);
                    }
                    callResult = callResult + call_s;

                }
                System.out.println("****************************Handle response****************************\n");
                System.out.println(callResult);
                Date respDate = new Date();
                if(callResult == 0) {
                    Response resp = new Response();
                    resp.setRequestId(request_id);
                    resp.setCsruServiceId(CSRU_Service_id);
                    resp.setResponseDate(respDate);
                    resp.setOutputDir(outDir);
                    //factory.session.save(resp);
                    factory.insertIntoResponse(resp);
                    return_value = "OK|0|" + request_id + "|";
                } else {
                    //factory.close();
                    return_value =  "ERROR|2|" + request_id + "|";
                }


            }



        } catch (Exception e) {
            return_value =   e.getMessage() + "|3|"  + request_id + "|";

        } finally {
            //factory.close();
            factory.commit();
            //factory.close();
        }

        return return_value;
    }

}

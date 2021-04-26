package com.dxc.soapcall;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

import com.hp.sk.ru.verejnost.konanie.datatypes.GetZoznamSudovRequest;
import com.hp.sk.ru.verejnost.konanie.datatypes.GetZoznamSudovResponse;
import com.hp.sk.ru.verejnost.konanie.datatypes.KonanieServicePort;
import com.hp.sk.ru.verejnost.konanie.datatypes.KonanieServicePortService;
/*NOVA MIKROSLUZBA instancia I_getZoznamSudov od providera RU*/


public class I_getZoznamSudov {


	
	private URL wsdlURL;
	/*vytvorim premenne pre Request aj pre Response NOVEJ MIKROSLUZBY I_getZoznamSudov*/
	private GetZoznamSudovRequest GetZoznamSudovReq;
	private GetZoznamSudovResponse GetZoznamSudovResp;
	
	private static final QName SERVICE_NAME = new QName("datatypes.konanie.verejnost.ru.sk.hp.com", "KonanieServicePortService");
    
	/*vytvorim novu instaciu MIKROSLUZBY I_getZoznamSudov a tiez instancie pre Request a Response*/
	I_getZoznamSudov() {
		GetZoznamSudovReq = new GetZoznamSudovRequest();
		GetZoznamSudovResp = new GetZoznamSudovResponse();
	}
	
	public GetZoznamSudovResponse getGetZoznamSudovResponse() {
		return this.GetZoznamSudovResp;
	}
	
	
	public URL getURL() {
		return wsdlURL;
	} 
	public void setURL(String SwsdlURL) throws MalformedURLException {
		this.wsdlURL = new URL(SwsdlURL);	
	}
	/* funkcia na ziskanie odpovedi get...Response */
	/* metody pre nastavenie parametrov na zaklade wsdl... ?? datatype GetZoznamSudovRequest neobsahuje ziadne parametre??*/
	public void setGetZoznamSudovRequest(GetZoznamSudovRequest req) {
		this.GetZoznamSudovReq = req;
	}
	public GetZoznamSudovRequest getGetZoznamSudovRequest() {
		return this.GetZoznamSudovReq;
	}
	
	
	public int callService() {
		try {
			KonanieServicePortService ss = new KonanieServicePortService(wsdlURL, SERVICE_NAME);
			KonanieServicePort port = ss.getKonanieServicePortSoap11();
			System.out.println("Invoking soapcall...");
			/* this.<objekt response> = port.<nazov sluzby>(this.<objektRequestu>); */
			this.GetZoznamSudovResp = port.getZoznamSudov(this.GetZoznamSudovReq);
		} catch (Exception e) {
            return 1;
        }
		return 0;
	}

}

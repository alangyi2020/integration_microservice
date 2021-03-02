package com.dxc.soapcall;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import com.hp.sk.ru.verejnost.konanie.datatypes.GetKonanieDetailRequest;
import com.hp.sk.ru.verejnost.konanie.datatypes.GetKonanieDetailResponse;
import com.hp.sk.ru.verejnost.konanie.datatypes.KonanieServicePort;
import com.hp.sk.ru.verejnost.konanie.datatypes.KonanieServicePortService;

public class I_getKonanieDetail {
	private URL wsdlURL;
	private GetKonanieDetailRequest GetKonanieDetailReq;
	private GetKonanieDetailResponse GetKonanieDetailResp;
	private static final QName SERVICE_NAME = new QName("datatypes.konanie.verejnost.ru.sk.hp.com", "KonanieServicePortService");
	I_getKonanieDetail() {
		GetKonanieDetailReq = new GetKonanieDetailRequest();
		GetKonanieDetailResp = new GetKonanieDetailResponse();
	}
	
	public GetKonanieDetailResponse getGetKonanieDetailResponse() {
		return this.GetKonanieDetailResp;
	}

	
	public URL getURL() {
		return wsdlURL;
	} 
	public void setURL(String SwsdlURL) throws MalformedURLException {
		this.wsdlURL = new URL(SwsdlURL);	
	}
	
	public void setGetKonanieDetailRequest(GetKonanieDetailRequest req) {
		this.GetKonanieDetailReq = req;
	}
	public GetKonanieDetailRequest getGetKonanieDetailRequest() {
		return this.GetKonanieDetailReq;
	}
	
	public void setkonanieId(String konanieId) {		
		this.GetKonanieDetailReq.setKonanieId(konanieId);		
	}
	public String getkonanieId() {
		return this.GetKonanieDetailReq.getKonanieId();
	}
	
	public int callService() {
		try {
			KonanieServicePortService ss = new KonanieServicePortService(wsdlURL, SERVICE_NAME);
			KonanieServicePort port = ss.getKonanieServicePortSoap11();
			System.out.println("Invoking vyhladajKonanie...");
			this.GetKonanieDetailResp = port.getKonanieDetail(this.GetKonanieDetailReq);
		} catch (Exception e) {
            return 1;
        }
		return 0;
	}

}


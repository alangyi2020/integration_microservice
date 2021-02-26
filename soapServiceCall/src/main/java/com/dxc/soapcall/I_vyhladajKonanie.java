package com.dxc.soapcall;

import com.hp.sk.ru.verejnost.konanie.datatypes.VyhladajKonanieRequest;
import com.hp.sk.ru.verejnost.konanie.datatypes.VyhladajKonanieResponse;
import com.hp.sk.ru.verejnost.konanie.datatypes.KonanieServicePortService;
import com.hp.sk.ru.verejnost.konanie.datatypes.KonanieServicePort;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

public class I_vyhladajKonanie {
	private URL wsdlURL;
	private VyhladajKonanieRequest VyhladajKonanieReq;
	private VyhladajKonanieResponse VyhladajKonanieResp;
	private static final QName SERVICE_NAME = new QName("datatypes.konanie.verejnost.ru.sk.hp.com", "KonanieServicePortService");
	
	
	


	I_vyhladajKonanie() {
		VyhladajKonanieReq = new VyhladajKonanieRequest();
		//set default values
		this.setStranka("0");
		this.setVysledkovNaStranku("10");
		this.setTypTriedenia("DatumPoslednejUdalosti");
		
	}
	
	public VyhladajKonanieResponse getVyhladajKonanieResponse() {
		return this.VyhladajKonanieResp;
	}

	
	public URL getURL() {
		return wsdlURL;
	} 
	public void setURL(String SwsdlURL) throws MalformedURLException {
		this.wsdlURL = new URL(SwsdlURL);
		//this.wsdlURL = new URL("https://ru-ws.justice.sk/ru-verejnost-ws/konanieService.wsdl");
		 //System.out.println(wsdlURL);  		
	}
	
	public VyhladajKonanieRequest getVyhladajKonanie() {
		return VyhladajKonanieReq;
	}
	
	public void setVyhladajKonanie(VyhladajKonanieRequest vhr ) {
		this.VyhladajKonanieReq = vhr;		
	}

	
	public void setQuery(String query) {		
		this.VyhladajKonanieReq.setQuery(query);
	}
	public void setStranka(String stranka) {
		this.VyhladajKonanieReq.setStranka(Integer.parseInt(stranka));		
	}
	public void setVysledkovNaStranku(String VysledkovNaStranku) {
		this.VyhladajKonanieReq.setVysledkovNaStranku(Integer.parseInt(VysledkovNaStranku));
	}
	
	public void setTypTriedenia(String TypTriedenia) {
		if(TypTriedenia.toUpperCase().equals("RELEVANCIA")  ) {			 
			this.VyhladajKonanieReq.setTypTriedenia(com.hp.sk.ru.verejnost.konanie.datatypes.TypTriedenia.RELEVANCIA);
		}
		if(TypTriedenia.toUpperCase().equals("DATUM_ZACATIA_KONANIA"))
			this.VyhladajKonanieReq.setTypTriedenia(com.hp.sk.ru.verejnost.konanie.datatypes.TypTriedenia.DATUM_ZACATIA_KONANIA);
		if(TypTriedenia.toUpperCase().equals("DATUM_ZACATIA_KONANIA"))
			this.VyhladajKonanieReq.setTypTriedenia(com.hp.sk.ru.verejnost.konanie.datatypes.TypTriedenia.DATUM_POSLEDNEJ_UDALOSTI);
		
		
	}
	public int callService() {
		try {
			//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaInvoking vyhladajKonanie...");
			KonanieServicePortService ss = new KonanieServicePortService(wsdlURL, SERVICE_NAME);
			KonanieServicePort port = ss.getKonanieServicePortSoap11();
			System.out.println("Invoking vyhladajKonanie...");
			this.VyhladajKonanieResp = port.vyhladajKonanie(VyhladajKonanieReq);
		} catch (Exception e) {
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa elqrtam");
			System.out.println(e.toString());
            return 1;
        }
		return 0;
	}

}

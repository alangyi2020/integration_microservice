package com.dxc.soapcall;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import com.hp.sk.ru.verejnost.konanie.datatypes.KonanieServicePort;
import com.hp.sk.ru.verejnost.konanie.datatypes.KonanieServicePortService;

public class I_Service_template {
	private URL wsdlURL;
	/*todo: 
	 * deklaracia objektov typu sluzba
	 */
	private static final QName SERVICE_NAME = new QName("datatypes.konanie.verejnost.ru.sk.hp.com", "KonanieServicePortService");
	/*TODO
	 * vytvorenie konstruktora
	 * instancia requestu a response danej sluzby
	 * <objekt response>
	 * <objektRequestu>
	 */
	
	public URL getURL() {
		return wsdlURL;
	} 
	public void setURL(String SwsdlURL) throws MalformedURLException {
		this.wsdlURL = new URL(SwsdlURL);	
	}
	/*TODO
	 * vytvorenie funkcie na ziskanie odpovedi get...Response
	 */
	/*TODO
	 * vytvorenie metod pre nastavenie parametrov na zaklade wsdl
	 */
	
	public int callService() {
		try {
			KonanieServicePortService ss = new KonanieServicePortService(wsdlURL, SERVICE_NAME);
			KonanieServicePort port = ss.getKonanieServicePortSoap11();
			System.out.println("Invoking soapcall...");
			/*TODO
			 * Upravit podla wsdl:
			 * this.<objekt response> = port.<nazov sluzby>(this.<objektRequestu>);
			 */
			
		} catch (Exception e) {
            return 1;
        }
		return 0;
	}

}

/**
 * 
 */


import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tscp.mvno.smpp.domain.SMSMessage;
import com.tscp.mvno.smpp.webservice.SMSAppService;
import com.tscp.mvno.smpp.webservice.SMSAppWs;
import com.tscp.mvno.smpp.webservice.TSCPSMPPResponse;
import com.tscp.mvno.smpp.webservice.TscpsmppResponse;

/**
 * @author yliu
 *
 */
public class SmsWSTest {
	
	public final static String serviceName = "SMSAppService";
	public final static String nameSpace = "http://webservice.smpp.mvno.tscp.com/";
	public final static String WSDLlocation = "http://uscaelmux14:60798/SMSWebService/SMSAppService?wsdl";
	
	static SMSAppService service;
	private SMSAppWs port;
    
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {		
	}

	@Test
	public void testSMCCConnection(){
		
	}
	
	@Test
	public void testSMSWebServiceLocation(){
	   try{	
		  service = new SMSAppService(new URL(WSDLlocation), new QName(nameSpace, serviceName));
		  assertNotNull(service);
		  //assertEquals(service.getServiceName(), {nameSpaceSMSAppWs}/serviceName);
       }
	   catch (Exception e) {
         e.printStackTrace();
         service = new SMSAppService();
       } 
  }

  @Test
  public void testGetPort(){
	//Retrieves a proxy to the service
	port = service.getSMSAppPort();
	assertNotNull(port);
	//assertEquals(port.getClass(), SMSAppWs.class);
	    
  }	
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

}

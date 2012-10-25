/**
 * 
 */


import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPFaultException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tscp.mvno.smpp.domain.SMSMessage;
import com.tscp.mvno.smpp.exception.SmsInputException;
import com.tscp.mvno.smpp.webservice.SMSAppService;
import com.tscp.mvno.smpp.webservice.SMSAppWs;
import com.tscp.mvno.smpp.webservice.SmsMessage;
import com.tscp.mvno.smpp.webservice.TSCPSMPPResponse;
import com.tscp.mvno.smpp.webservice.TscpsmppResponse;

/**
 * @author yliu
 *
 */
public class SmsWSFunctionalTest {

	private String destinationTN = "2132566431";
	private String nonSprintDestinationTN = "6268636679";
	private String wrongDigitDestinationTN = "21325664314631";
	private String testMessage = "test";
	private SMSMessage sms;
	private SMSMessage sms2;
	
	public final static String serviceName = "SMSAppService";
	public final static String nameSpace = "http://webservice.smpp.mvno.tscp.com/";
	public final static String WSDLlocation = "http://uscaelmux14:60798/SMSWebService/SMSAppService?wsdl";
	
	private static SMSAppService service;
	private static SMSAppWs port;
    
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try{	
			service = new SMSAppService(new URL(WSDLlocation), new QName(nameSpace, serviceName));			  
			port = service.getSMSAppPort();
	    }
		catch (Exception e) {
	         e.printStackTrace();
	    } 
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {		
		sms = new SMSMessage(destinationTN, (testMessage + 1));
    	sms2 = new SMSMessage(destinationTN, (testMessage + 2));
    	List<SMSMessage> messageList= new ArrayList<SMSMessage>();
    	messageList.add(sms);
    	messageList.add(sms2);
	}

	@Test	
	public void testSMSMessageString() {
    	//TSCPSMPPResponse r = null;
    	TscpsmppResponse r = null;
    	System.out.println("****** testSMSMessageString ******");
    	try {
            r = port.sendSMSMessageString(destinationTN, testMessage);
            assertNotNull(r);
            assertNotNull(r.getMessageId());
            assertEquals(r.getCommandStatus(), 0);
            
            System.out.println("r.getCommandStatus = " + r.getCommandStatus());
            System.out.println("r.getCommandId() = " + r.getCommandId());
            System.out.println("r.getErrorCode = " + r.getErrorCode());
            System.out.println("r.getMessageId() = " + r.getMessageId());
            System.out.println("r.getMessageStatus() = " + r.getMessageStatus());
            
            System.out.println("r.getMessageText = " + r.getMessageText());
            System.out.println("r.getMessageLen() = " + r.getMessageLen());
            System.out.println("r.getProtocalId() = " + r.getProtocolID());
            System.out.println("r.getServiceType() = " + r.getServiceType());
            System.out.println("r.getLength = " + r.getLength());
        } catch(Exception e) {
            e.printStackTrace();
        }        
	}   
    
	@Test	
    public void testSMSMessageStringWithNonSprintTN() {
       	TscpsmppResponse r = null;
       	System.out.println("****** testSMSMessageStringWithNonSprintTN ******");
       	try {
             r = port.sendSMSMessageString(nonSprintDestinationTN, testMessage);
             assertNotNull(r);
             assertNull(r.getMessageId());
             assertEquals(r.getCommandStatus(), 11);
                
                System.out.println("r.getCommandStatus = " + r.getCommandStatus());
                System.out.println("r.getCommandId() = " + r.getCommandId());
                System.out.println("r.getErrorCode = " + r.getErrorCode());
                System.out.println("r.getMessageId() = " + r.getMessageId());
                System.out.println("r.getMessageStatus() = " + r.getMessageStatus());
                System.out.println("r.getMessageText = " + r.getMessageText());
                System.out.println("r.getMessageLen() = " + r.getMessageLen());
                System.out.println("r.getProtocalId() = " + r.getProtocolID());
                System.out.println("r.getServiceType() = " + r.getServiceType());
                System.out.println("r.getLength = " + r.getLength());
        } 
       	catch(Exception e) {
              e.printStackTrace();
        }        
    }
        
     @Test	
     public void testSMSMessage() {
        	//TSCPSMPPResponse r = null;
    	 SmsMessage smsMessage = new SmsMessage(); 
    	 smsMessage.setDestinationTN(destinationTN);
    	 smsMessage.setMessageText(testMessage);
         TscpsmppResponse r = null;
         System.out.println("testSMSMessage");
         try {
                r = port.sendSMSMessage(smsMessage);
                assertNotNull(r);
                assertNotNull(r.getMessageId());
                assertEquals(r.getCommandStatus(), 0);
                
                System.out.println("r.getCommandStatus = " + r.getCommandStatus());
                System.out.println("r.getCommandId() = " + r.getCommandId());
                System.out.println("r.getErrorCode = " + r.getErrorCode());
                System.out.println("r.getMessageId() = " + r.getMessageId());
                System.out.println("r.getMessageStatus() = " + r.getMessageStatus());
                
                System.out.println("r.getMessageText = " + r.getMessageText());
                System.out.println("r.getMessageLen() = " + r.getMessageLen());
                System.out.println("r.getProtocalId() = " + r.getProtocolID());
                System.out.println("r.getServiceType() = " + r.getServiceType());
                System.out.println("r.getLength = " + r.getLength());
          } 
         catch(Exception e) {
               e.printStackTrace();
         }        
     }   
      
     @Test	
     public void testSMSMessageWithNonSprintTN() {
           	SmsMessage smsMessage = new SmsMessage(); 
           	smsMessage.setDestinationTN(nonSprintDestinationTN);
           	smsMessage.setMessageText(testMessage);
           	TscpsmppResponse r = null;
           	System.out.println("****** testSMSMessageWithNonSprintTN ******");
           	try {
                 r = port.sendSMSMessage(smsMessage);
                 assertNotNull(r);
                 assertNull(r.getMessageId());
                 assertEquals(r.getCommandStatus(), 11);
                   
                    System.out.println("r.getCommandStatus = " + r.getCommandStatus());
                    System.out.println("r.getCommandId() = " + r.getCommandId());
                    System.out.println("r.getErrorCode = " + r.getErrorCode());
                    System.out.println("r.getMessageId() = " + r.getMessageId());
                    System.out.println("r.getMessageStatus() = " + r.getMessageStatus());
                    System.out.println("r.getMessageText = " + r.getMessageText());
                    System.out.println("r.getMessageLen() = " + r.getMessageLen());
                    System.out.println("r.getProtocalId() = " + r.getProtocolID());
                    System.out.println("r.getServiceType() = " + r.getServiceType());
                    System.out.println("r.getLength = " + r.getLength());
            }
           	catch(Exception e) {
                    e.printStackTrace();
            }        
    }  
            
    @Test(expected=SOAPFaultException.class)//(expected=SmsInputException.class)
    public void testSmsInputExceptionWithWrongDigitDestinationTN() throws Exception{  	
       	System.out.println("****** testSMSMessageString ******");
    	port.sendSMSMessageString(wrongDigitDestinationTN, testMessage);
    }
	
    
    /**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

}

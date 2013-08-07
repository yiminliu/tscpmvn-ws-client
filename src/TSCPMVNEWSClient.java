/**
 * 
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

import com.tscp.mvne.Account;
import com.tscp.mvne.Customer;
import com.tscp.mvne.Device;
import com.tscp.mvne.ServiceInstance;
import com.tscp.mvne.TruConnect;
import com.tscp.mvne.TruConnectService;
import com.tscp.mvne.NetworkInfo;



/**
 * @author 
 *
 */
public class TSCPMVNEWSClient {
	
	public static final String serviceName = "TruConnectService";
	public static final String nameSpace ="http://mvne.tscp.com/";
	public static final String WSDLlocation = "http://10.10.30.190:8080/TSCPMVNE/TruConnectService?wsdl";
	//public static final String WSDLlocation = "http://10.10.30.188:8080/TSCPMVNE-refund/TruConnectService?wsdl";

	
	private static boolean initialized = false;
	
	private static TruConnectService service;
    private static TruConnect port;
	
 
	static{
		init();
		initialized = true;
	}
	
	public static void main(String[] args){
		
		if(!initialized)
			init();
		
		//client.createBillingAccount();
		//activateDevice(21580, "09115427139");
		createServiceInstance(784345, "2139256996");
		//reactivateAccount(19702, "09115426726");
		
		//deactivateService("8186934505");
		//reinstallCustomerDevice(16552, 763391, 29376, "09115405482");
	    
		
	   
	    //******* It activate the device first, then createServiceInstance in Kenan ******/		
	   //activateDeviceAndCreateServiceInstance(14990, "09115382363", 756959);
		//createServiceInstance(756959, "8186935633");
	   //suspendAccount(15161, 760056, 27968);
		//addCreditCard();
		//getCreditCardDetail();
		//makePaymentById();
		//updateCreditCardPaymentMethod();
		//deleteCreditCard();
				
		System.out.println("Done");
		//client.activateSubscription(null, "09115392774");
		
	}
		
	
	private static void init() {	
	   try{	
		  service = new TruConnectService(new URL(WSDLlocation), new QName(nameSpace, serviceName));
		}
		catch (Exception e) {
	        e.printStackTrace();
	        service = new TruConnectService();
	    } 
		port = service.getTruConnectPort(); 
	}
		
	//This only activate device
	public static com.tscp.mvne.NetworkInfo activateDevice(int customerId, String esn){
		Customer customer = new Customer();
		customer.setId(customerId);
		com.tscp.mvne.NetworkInfo networkInfo = reserveMDN();
		networkInfo.setEsnmeiddec(esn);
		com.tscp.mvne.NetworkInfo resultNetworkInfo = null;
		try{
			resultNetworkInfo = port.activateService(customer, networkInfo);
		}
		catch(WebServiceException wse) {
			   wse.printStackTrace();
		}
		System.out.println("Device is activated with MDN: " + resultNetworkInfo.getMdn());
		return resultNetworkInfo;		
	}	
	
	
	//This method is used to create a Kenan service instance (seervice/package/component).
	//It is usually used in the case of device was activated and Kenan account was created, but no Kenan service is created yet.  
	public static void createServiceInstance(int accountNo, String mdn){
		   Account account = new Account();
		   account.setAccountno(accountNo);
		   ServiceInstance serviceInstance = new ServiceInstance();
		   serviceInstance.setExternalId(mdn);
		   Account newAccount = null;
		   try {
		       newAccount = port.createServiceInstance(account, serviceInstance);
		   }
		   catch(WebServiceException wse) {
			   wse.printStackTrace();
		   }
		   System.out.println("New Service = " + service.toString());
		   for (ServiceInstance service : newAccount.getServiceinstancelist()){
			   System.out.println("MDN = " + service.getExternalId());	
			   System.out.println("Active date = " + service.getActiveDate());
		   }
		   
		   for (com.tscp.mvne.Package pack : newAccount.getPackageList()){
			   System.out.println("Package name = " + pack.getName());
			   for (com.tscp.mvne.Component comp : pack.getComponentList()){
				   if(comp != null)
				      System.out.println("Component name = " + comp.getName());
			   }
		   }
		}
	
	//This is to activate device and then create Kenan service instance. Namely, it combines the activateDevice()
	//and createServiceInstance methods
	public static void activateDeviceAndCreateServiceInstance(int customerId, String esn, int accountNo){	
		com.tscp.mvne.NetworkInfo networkInfo = activateDevice(customerId, esn);		
		createServiceInstance(accountNo, networkInfo.getMdn());
	}	
	
	//This method is used to either reinstall device (when status_id =3) or restore account (when status_id=5)
	//if customer has only one device, the esn input can be null)
	public static void reactivateAccount(int custId, String esn) {
		Customer customer = new Customer();
		customer.setId(custId);
		
		List<Device> deviceList = null;
		
		try {
			deviceList = port.getDeviceList(customer);
		}
		catch(WebServiceException wse) {
		   wse.printStackTrace();
		}
		int status = 0;
		for(Device d : deviceList){
			status = d.getStatusId();
			System.out.println("ESN " + d.getValue() +"'s Status = " + d.getStatusId());
			if(status == 3 ) {
			   if((esn != null || esn.length() == 0) && esn.equals(d.getValue())) {	
				  System.out.println("Reinstalling device: " + d.getValue()); 
  			      try {
		            port.reinstallCustomerDevice(customer, d);
		            System.out.println(d.getValue() + " reinstalled."); 
		          }
		          catch(WebServiceException wse) {
		             wse.printStackTrace();
		          }
			   }
			}   
			else if (status == 5) {
				System.out.println("ESN = " + esn + ". Value = " + d.getValue());
				if((esn != null || esn.length() == 0) && esn.equals(d.getValue())) {		
				   System.out.println("Restore account: " + d.getAccountNo()); 	
	  			   try {
			            port.restoreAccount(custId, d.getAccountNo(), d.getId());
			            System.out.println(d.getAccountNo() + " restored.");
			       }
			       catch(WebServiceException wse) {
			           wse.printStackTrace();
			       }
			       catch(Exception e) {
			           e.printStackTrace();
			       }
				}   
			}
		}		
	}
	
	//This is used to deactivate a service
	public static void deactivateService(String mdn) {
		if(mdn == null || mdn.length() != 10)
		   System.out.println("Pease enter MDN");
		
		ServiceInstance serviceInstance = new ServiceInstance();
		serviceInstance.setExternalId(mdn);
		
		try {
			port.disconnectService(serviceInstance);	
			System.out.println("MDN::"+ mdn +" deactivated");
		}
		catch(WebServiceException wse) {
	           wse.printStackTrace();
	    }		
	}
	
	public static void suspendAccount(int custId, int accountNo, int deviceId) {
			
		try {
			port.suspendAccount(custId, accountNo, deviceId);			
		}
		catch(WebServiceException wse) {
	           wse.printStackTrace();
	    }
		catch(Exception e) {
	           e.printStackTrace();
	    }
		System.out.println("MDN::"+ accountNo +" suspended");
		
	}
	
	private static void reinstallCustomerDevice(int custId, int accountNo, int deviceId, String esn) {
			Customer customer = new Customer();
			customer.setId(custId);
			
			Device device = new Device();
	        device.setId(deviceId);
	        device.setAccountNo(accountNo);
	        device.setValue(esn);
	        try{
	           port.reinstallCustomerDevice(customer, device);
	        }
	        catch(WebServiceException wse) {
		           wse.printStackTrace();
		    }
			catch(Exception e) {
		           e.printStackTrace();
		    }   
	}		
	

	private static com.tscp.mvne.NetworkInfo reserveMDN(){
	   return port.reserveMDN();
	}
	
	/*
	//This only activate device
	public static com.tscp.mvne.NetworkInfo swapDevice(int customerId, String esn){
		Customer customer = new Customer();
		customer.setId(customerId);
		com.tscp.mvne.NetworkInfo networkInfo = reserveMDN();
		networkInfo.setEsnmeiddec(esn);
		com.tscp.mvne.NetworkInfo resultNetworkInfo = null;
		try{
			resultNetworkInfo = port.swapDevice(arg0, arg1, arg2).activateService(customer, networkInfo);
		}
		catch(WebServiceException wse) {
			   wse.printStackTrace();
		}
		return resultNetworkInfo;		
	}	
	*/
	
	
	/********** The following are methods to test the new chanages of encrypted CC on database *********/
	public static void addCreditCard(){
		com.tscp.mvne.CreditCard cc = new com.tscp.mvne.CreditCard();
		cc.setAddress1("test");
		cc.setCity("city");
		cc.setState("CA");
		cc.setZip("71007");
		cc.setCreditCardNumber("4387755555555550");
		cc.setExpirationDate("1213");
		cc.setNameOnCreditCard("test");
		cc.setPaymentid(0);
		cc.setVerificationcode("999");
		cc.setIsDefault("Y");
		
		int custId = 547; 	
		Customer customer = new Customer();
		customer.setId(custId);
		
		port.addCreditCard(customer, cc);
	}
	
	public static com.tscp.mvne.CreditCard getCreditCardDetail(){
		
		com.tscp.mvne.CreditCard cc = port.getCreditCardDetail(921);
		System.out.println("CC No= "+ cc.getCreditCardNumber());
		System.out.println("CC Exp Date = "+ cc.getExpirationDate());
		System.out.println("CC Sec Code = "+ cc.getVerificationcode());
		
		return cc;
	}
	
	public static void updateCreditCardPaymentMethod(){
    	int customerId = 547;
    	Customer customer = new Customer();
    	customer.setId(customerId);
    	    
		int paymentId = 1965;
		String expirationDate = "1213";
		String securityCode ="999";
		String zip = "90071";
		
		com.tscp.mvne.CreditCard creditCard = new com.tscp.mvne.CreditCard();
	    creditCard.setPaymentid(paymentId);
	    creditCard.setNameOnCreditCard("Test Test");
	    creditCard.setCreditCardNumber("4387755555555552");
	    creditCard.setExpirationDate(expirationDate);
	    creditCard.setVerificationcode(securityCode);
	    creditCard.setAddress1("Test St.");	
	    creditCard.setCity("Los Angeles");
	    creditCard.setState("CA");
	    creditCard.setZip(zip);	      
	    creditCard.setAlias("5550");
	    creditCard.setIsDefault("Y");
	   
	    
	    List<com.tscp.mvne.CustPmtMap> map = port.updateCreditCardPaymentMethod(customer, creditCard);
	    
	    System.out.println("num of cutPmtMap returned= "+ map.size());
    }	
	
	
	 public static void deleteCreditCard(){
	    int customerId = 547;
	   	Customer customer = new Customer();
	   	customer.setId(customerId);
	    	    
		int paymentId = 1966;
			
		port.deleteCreditCardPaymentMethod(customer, paymentId);
	 }	
			
	    
	
    public static void makePaymentById(){
	    String sessionId = "test000"; 
	    int custId = 547; 
	    int accountNo = 695482;
	    int paymentId = 1966;
	    String amount = "10.00";
	
	    port.makePaymentById(sessionId, custId, accountNo, paymentId, amount);
	
	}

    
	
	public void createBillingAccount(){
	   int cust_id = 17838; 	
	   int accountNo = 767706;
	   String mdn = "2139266795";
	   Customer customer = new Customer();
	   customer.setId(cust_id);
	   Account account = new Account();
	   account.setAccountno(accountNo);
	   ServiceInstance serviceInstance = new ServiceInstance();
	   serviceInstance.setExternalId(mdn);
	   try {
	       port.createBillingAccount(customer, account);
	   }
	   catch(WebServiceException wse) {
		   wse.printStackTrace();
	   }
	}
}

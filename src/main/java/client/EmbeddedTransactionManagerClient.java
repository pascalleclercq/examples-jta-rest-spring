package client;

import inventory.PurchaseRequest;
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.codehaus.jackson.jaxrs.JacksonJsonProvider;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import payment.Payment;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.rest.spring.TransactionAwareRestClientInterceptor;

public class EmbeddedTransactionManagerClient {
	static String cardno = "card10";
	

	
	public static void main(String[] args) throws Exception {
		TransactionAwareRestClientInterceptor clientInterceptor = new TransactionAwareRestClientInterceptor();
		
		RestTemplate paymentClient = new RestTemplate();
		paymentClient.getInterceptors().add(clientInterceptor);
		RestTemplate inventoryClient = new RestTemplate();
		inventoryClient.getInterceptors().add(clientInterceptor);

		UserTransactionManager utm = new UserTransactionManager();
		utm.init();

		callBothInJtaTransaction(paymentClient, inventoryClient);
	}


	private static void callBothInJtaTransaction(RestTemplate paymentClient, RestTemplate inventoryClient) throws Exception {
		
		UserTransactionManager utm = new UserTransactionManager();
		utm.begin();
		pay(cardno, paymentClient);
		
		updateInventory(cardno, inventoryClient);
		utm.commit();
		
	}
	
	
	private static void pay(String cardno, RestTemplate paymentClient) {
		Payment payment = new Payment();
		payment.amount =10;
		payment.cardno = cardno;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		//set your entity to send
		HttpEntity<Payment> entity = new HttpEntity<>(payment,headers);
		Payment p =	paymentClient.postForObject("http://localhost:9092/payment/pay", entity, Payment.class);
		System.out.println(p);
	}
	
	

	private static void updateInventory(String cardno,
			RestTemplate inventoryClient) {
		PurchaseRequest purchaseRequest = new PurchaseRequest();
		purchaseRequest.cardno=cardno;
		purchaseRequest.itemId=20;
		purchaseRequest.qty=2;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		//set your entity to send
		HttpEntity<PurchaseRequest> entity = new HttpEntity<>(purchaseRequest,headers);
		
		inventoryClient.postForObject("http://localhost:9091/inventory/purchase", entity, Void.class);
		
//		
		
	}
}

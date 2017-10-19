package payment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atomikos.logging.Logger;
import com.atomikos.logging.LoggerFactory;

@RestController("payment")
@RequestMapping(path="/payment")
public class PaymentResource {

	private static final Logger LOGGER = LoggerFactory.createLogger(PaymentResource.class);
	
	@Autowired
	PaymentJdbc paymentJdbc;

	public PaymentResource() {
		System.out.println("PaymentResource");
	}
	@RequestMapping(path="/ping", method=RequestMethod.GET)
	public ResponseEntity<String> ping() {
		String response =  String.valueOf(System.currentTimeMillis());
		
		return ResponseEntity.ok().body(response);
	}
	
	/**
	 * @see PaymentServer
	 */
	@RequestMapping(path="/pay", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pay(@RequestBody Payment payment) throws Exception {
		paymentJdbc.performPayment(payment);
		return ResponseEntity.ok().body(payment);
	}


	@PostConstruct
	public void populateDb()  {
		try {
			paymentJdbc.populateDb();
		} catch (Exception e) {
			LOGGER.logError("logCloud not running ?", e);
		}
	}

}

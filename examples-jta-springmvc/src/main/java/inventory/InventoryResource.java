package inventory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atomikos.logging.Logger;
import com.atomikos.logging.LoggerFactory;

@RestController("inventory")
@RequestMapping(path="/inventory")
public class InventoryResource {

	private static final Logger LOGGER = LoggerFactory.createLogger(InventoryResource.class);

	@Autowired
	InventoryJdbc inventoryJdbc;

	@RequestMapping(path="/stock/{itemId}", method=RequestMethod.GET)
	public ResponseEntity<?> getStock(@PathVariable("itemId") int itemId) {
			try {
				int	res = inventoryJdbc.getStock(itemId);
				return ResponseEntity.ok(res);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
			}
	}

	@RequestMapping(path="/purchase", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> purchase(@RequestBody PurchaseRequest purchaseRequest) throws Exception {
		System.err.println(purchaseRequest);
			inventoryJdbc.purchase(purchaseRequest);
			return ResponseEntity.accepted().build();
	}

	@PostConstruct
	public void setup() {
		try {
			inventoryJdbc.setup();
		} catch (Exception e) {
			LOGGER.logError("logCloud not running ?", e);
		}
		
	}

}

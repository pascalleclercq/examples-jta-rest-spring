package inventory;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class InventoryJdbc {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void setup() throws Exception {
		try {
			jdbcTemplate.execute("select * from Stock");
		} catch (DataAccessException e) {
			System.err.println("Creating Stock table...");
			jdbcTemplate.update("create table Stock ( "
					+ " itemId DECIMAL ( 19 ,0 ), amount DECIMAL (19,0) , "
					+ " unitPrice DECIMAL ( 19, 0 ) )");
			for (int i = 0; i < 100; i++) {
				jdbcTemplate.update("insert into Stock values ( " + i
						+ ", 1000 , " + i + " )");
			}
		}
	}

	public void purchase(PurchaseRequest purchaseRequest) throws Exception {
		jdbcTemplate.update("update Stock set amount = amount - "
				+ purchaseRequest.qty + " where itemId = "
				+ purchaseRequest.itemId);
	}

	public int getStock(int itemId) throws Exception {

		int res = jdbcTemplate.queryForObject("select amount from Stock "
				+ " where itemId = " + itemId, Integer.class);
		return res;
	}

}

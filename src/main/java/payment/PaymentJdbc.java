package payment;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class PaymentJdbc {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@PostConstruct
	public void populateDb() throws Exception {
		try {
			jdbcTemplate.execute("select * from Payments");	
		
	} catch (DataAccessException e) {
		System.err.println("Creating Payments table...");
		jdbcTemplate.update("create table Payments (  cardno VARCHAR ( 20 ), amount DECIMAL (19,0) )");
		for (int i = 0; i < 100; i++) {
			jdbcTemplate.update("insert into Payments values ( " + "'card"+ i + "' , 0 )");
		}	
	}
	}

	public boolean performPayment(Payment payment) throws Exception {
		String sql = "update Payments set amount = amount + "
		+ payment.amount + " where cardno ='" + payment.cardno
		+ "'";
		jdbcTemplate.update(sql);
		return true;
	}
}

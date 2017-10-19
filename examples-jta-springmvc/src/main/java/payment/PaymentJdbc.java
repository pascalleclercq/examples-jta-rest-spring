package payment;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentJdbc {

	@Autowired
	private DataSource ds;

	private Connection getConnection() throws Exception {
		Connection conn = null;
		conn = ds.getConnection();

		return conn;

	}

	

	private static void closeConnection(Connection conn, boolean error)
			throws Exception {
		if (conn != null)
			conn.close();

	}

	@PostConstruct
	public void populateDb() throws Exception {
		boolean error = false;
		Connection conn = null;

		try {
			conn = getConnection();
			Statement s = conn.createStatement();
			try {
				s.executeQuery("select * from Payments");
			} catch (SQLException ex) {
				// table not there => create it
				System.err.println("Creating Payments table...");
				s.executeUpdate("create table Payments (  cardno VARCHAR ( 20 ), amount DECIMAL (19,0) )");
				for (int i = 0; i < 100; i++) {
					s.executeUpdate("insert into Payments values ( " + "'card"+ i + "' , 0 )");
				}
			}
			s.close();
		} catch (Exception e) {
			error = true;
			throw e;
		} finally {
			closeConnection(conn, error);

		}
	}

	public boolean performPayment(Payment payment) throws Exception {
		boolean error = false;

		Connection conn = null;

		try {
			conn = getConnection();
			Statement s = conn.createStatement();
			String sql = "update Payments set amount = amount + "
					+ payment.amount + " where cardno ='" + payment.cardno
					+ "'";
			int count = s.executeUpdate(sql);
			if (count == 0)
				throw new Exception("Invalid card: " + payment.cardno);

			s.close();
		} catch (Exception e) {
			error = true;
			throw e;
		} finally {
			try {
				closeConnection(conn, error);
			} catch (Exception e) {
				error = true;
			}
		}
		return error;
	}
}

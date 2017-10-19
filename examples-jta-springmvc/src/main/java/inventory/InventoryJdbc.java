package inventory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryJdbc {

	@PostConstruct
	public void setup() throws Exception {

		boolean error = false;
		Connection conn = null;
		try {
			conn = getConnection();
			Statement s = conn.createStatement();
			try {
				s.executeQuery("select * from Stock");
			} catch (SQLException ex) {
				// table not there => create it
				System.err.println("Creating Stock table...");
				s.executeUpdate("create table Stock ( "
						+ " itemId DECIMAL ( 19 ,0 ), amount DECIMAL (19,0) , "
						+ " unitPrice DECIMAL ( 19, 0 ) )");
				for (int i = 0; i < 100; i++) {
					s.executeUpdate("insert into Stock values ( " + i
							+ ", 1000 , " + i + " )");
				}
			}
			s.close();
		} catch (Exception e) {
			error = true;
			throw e;
		} finally {
			closeConnection(conn, true, error);

		}
	}

	/**
	 * Utility method to close the connection and terminate the transaction.
	 * This method does all XA related tasks and should be called within a
	 * transaction. When it returns, the transaction will be terminated.
	 *
	 * @param conn
	 *            The connection.
	 * @param terminateTransaction
	 *            If true, then the current tx will also be terminated.
	 * @param error
	 *            Indicates if an error has occurred or not. If true, the
	 *            transaction will be rolled back. If false, the transaction
	 *            will be committed.
	 */

	private void closeConnection(Connection conn, boolean terminateTransaction,
			boolean error) throws Exception {
		if (conn != null)
			conn.close();


	}

	/**
	 * Utility method to start a transaction and get a connection. This method
	 * should be called within a transaction.
	 *
	 * @return Connection The connection.
	 */

	private Connection getConnection() throws Exception {

		Connection conn = null;
		// retrieve the TM

		conn = ds.getConnection();

		return conn;

	}

	

	@Autowired
	private DataSource ds;

	public void purchase(PurchaseRequest purchaseRequest) throws Exception {
		boolean error = false;
		Connection conn = null;

		try {
			// start a transaction
			conn = getConnection();
			Statement s = conn.createStatement();
			String sql = "select unitPrice from Stock " + " where itemId = "
					+ purchaseRequest.itemId;
			ResultSet rs = s.executeQuery(sql);
			if (rs == null || !rs.next())
				throw new Exception("Item not found: " + purchaseRequest.itemId);
			int price = rs.getInt(1);
			sql = "update Stock set amount = amount - " + purchaseRequest.qty
					+ " where itemId = " + purchaseRequest.itemId;
			s.executeUpdate(sql);
			s.close();

		} catch (Exception e) {
			error = true;
			throw e;
		} finally {
			try {
				// close the connection and commit if appropriate
				closeConnection(conn, true, error);

				// since we had a top-level transaction,
				// the commit will have dissociated any thread
				// associations
			} catch (Exception e) {
				throw e;
			}
		}

	}

	public int getStock(int itemId) throws Exception {
		boolean error = false;
		// true ASA exception
		Connection conn = null;
		int res = -1;

		try {
			// start a transaction
			conn = getConnection();
			Statement s = conn.createStatement();
			String sql = "select amount from Stock " + " where itemId = "
					+ itemId;
			ResultSet rs = s.executeQuery(sql);
			if (rs == null || !rs.next())
				throw new Exception("Item not found: " + itemId);
			res = rs.getInt(1);

			s.close();
		} catch (Exception e) {
			error = true;
			throw e;
		} finally {
			try {
				// close the connection and commit if appropriate
				closeConnection(conn, true, error);
			} catch (Exception e) {
				throw e;
			}
		}
		return res;
	}
	
}

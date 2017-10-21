package inventory;

import java.util.Properties;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.rest.spring.TransactionAwareRestContainerFilter;

@SpringBootApplication
@PropertySource("classpath:inventory.properties")
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}
	
	@Bean
	public Filter transactionAwareRestContainerFilter() {
		TransactionAwareRestContainerFilter compressFilter = new TransactionAwareRestContainerFilter();
	    return compressFilter;
	}
	
	@Bean
	public AtomikosDataSourceBean dataSourceBean() {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setUniqueResourceName("inventory");
		ds.setXaDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
		Properties xaProperties = new Properties();
		xaProperties.setProperty("URL", "jdbc:h2:mem:inventory");
		xaProperties.setProperty("user", "sa");
		ds.setXaProperties(xaProperties);
		// OPTIONAL pool size
		ds.setPoolSize(5);
		// OPTIONAL timeout in secs between pool cleanup tasks
		ds.setBorrowConnectionTimeout(60);

		return ds;
	}
}

package payment;

import java.util.Properties;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import com.atomikos.http.spring.rest.TransactionAwareRestContainerFilter;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@SpringBootApplication
@PropertySource("classpath:payment.properties")
public class PaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}
	
	
	@Bean
	public Filter transactionAwareRestContainerFilter() {
		TransactionAwareRestContainerFilter compressFilter = new TransactionAwareRestContainerFilter();
	    return compressFilter;
	}
	
	@Bean
	public AtomikosDataSourceBean dataSourceBean() {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setUniqueResourceName("payment");
		ds.setXaDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
		Properties xaProperties = new Properties();
		xaProperties.setProperty("URL", "jdbc:h2:mem:payment");
		xaProperties.setProperty("user", "sa");
		ds.setXaProperties(xaProperties);
		// OPTIONAL pool size
		ds.setPoolSize(5);
		// OPTIONAL timeout in secs between pool cleanup tasks
		ds.setBorrowConnectionTimeout(60);

		return ds;
	}
}

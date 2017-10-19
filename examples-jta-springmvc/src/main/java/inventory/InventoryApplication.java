package inventory;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import com.atomikos.rest.interceptor.TransactionAwareRestContainerFilter;

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
}

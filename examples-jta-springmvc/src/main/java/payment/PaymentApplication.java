package payment;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import com.atomikos.rest.interceptor.TransactionAwareRestContainerFilter;

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
}

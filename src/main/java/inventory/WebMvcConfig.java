package inventory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.atomikos.http.api.Transaction;
import com.atomikos.http.spring.rest.AtomikosRestPort;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new HttpMessageConverter<Object>() {

			@Override
			public boolean canRead(Class<?> clazz, MediaType mediaType) {
				return true;
			}

			@Override
			public boolean canWrite(Class<?> clazz, MediaType mediaType) {
				return true;
			}

			@Override
			public List<MediaType> getSupportedMediaTypes() {
				return Collections.singletonList(MediaType
						.valueOf(Transaction.APPLICATION_VND_ATOMIKOS_JSON));
			}

			@Override
			public void write(Object t, MediaType contentType,
					HttpOutputMessage outputMessage) throws IOException,
					HttpMessageNotWritableException {
				// TODO Auto-generated method stub

			}

			@Override
			public Object read(Class<? extends Object> clazz,
					HttpInputMessage inputMessage) throws IOException,
					HttpMessageNotReadableException {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	

	@Bean
	public AtomikosRestPort atomikosRestPort() {
		return new AtomikosRestPort();
	}

}

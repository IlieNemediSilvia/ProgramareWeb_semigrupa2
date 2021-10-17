package demo8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public FibonacciFactoryBean factory(){
        return new FibonacciFactoryBean();
    }
}

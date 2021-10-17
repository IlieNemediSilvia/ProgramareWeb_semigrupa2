package demo1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationConfiguration {
    @Bean
    @Scope("prototype")
    public Country country(){
        return new Country("Romania", "Bucharest", "Europe");
    }
}

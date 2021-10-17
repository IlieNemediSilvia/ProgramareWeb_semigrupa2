package demo6;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ApplicationConfiguration {
    @Bean
    public Person president (@Value("Klaus Iohannis") String name){
        return new President(name);
    }
    @Bean
    public Person king (@Value("Norway's king") String name){
        return new King(name);
    }
}

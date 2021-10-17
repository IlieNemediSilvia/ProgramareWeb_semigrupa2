package demo7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ComponentScan
public class ApplicationConfiguration {
//    @Bean
//    public String largestCity(){
//        return "Bucharest";
//    }
//    @Bean
//    public String smallestCity(){
//        return "Tusnad";
//    }

    @Bean("largestCity")
    @Order(3)
    public String city1(){
        return "Bucharest";
    }
    @Bean("smallestCity")
    @Order(1)
    public String city2(){
        return "Tusnad";
    }
    @Bean
    @Order(2)
    public String thirdCity(){
        return "Timisoara";
    }
}

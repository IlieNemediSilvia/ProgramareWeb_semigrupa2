package demo2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Country firstCountry = context.getBean(Country.class);
        System.out.println("First: " + firstCountry);
    }
}

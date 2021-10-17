package demo5;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Country country = context.getBean(Country.class);
        City city = context.getBean(City.class);
        System.out.println(city);
        System.out.println(country);
    }
}

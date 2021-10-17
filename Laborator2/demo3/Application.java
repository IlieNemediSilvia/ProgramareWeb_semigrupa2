package demo3;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Country country = context.getBean(Country.class);
        System.out.println("Name: " + country.getName() + ", capital: " + country.getCapital().getName());
    }
}

package demo6;



import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Country republic = context.getBean(Republic.class);
        Country kingdom = context.getBean(Kingdom.class);
        System.out.println(republic);
        System.out.println(kingdom);
    }
}

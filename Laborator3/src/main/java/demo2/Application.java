package demo2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Settings settings = context.getBean(Settings.class);
        System.out.println(settings.getHost());
        System.out.println(settings.getUser());
    }
}

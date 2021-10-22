package demo4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Worker worker = context.getBean(Worker.class);
        worker.doSomething();
        worker.createSomething();
    }
}

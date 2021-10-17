package demo8;



import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        for(int i = 0; i<10; i++){
            System.out.println(context.getBean(Integer.class));
        }
    }
}

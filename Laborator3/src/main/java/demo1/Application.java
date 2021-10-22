package demo1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Person person = context.getBean(Person.class);
        person.setFirstName("Silvia");
        person.setLastName("Ilie");
        person.setGender('f');

        //Person person1 = new Person();
        //person1.setLastName("ABC");
    }
}

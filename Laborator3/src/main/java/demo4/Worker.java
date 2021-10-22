package demo4;

import org.springframework.stereotype.Component;

@Component
public class Worker {
    public void doSomething() {
        System.out.println("Executing doSomething..");

    }
    public void createSomething() {
        System.out.println("Executing createSomething..");
    }
}

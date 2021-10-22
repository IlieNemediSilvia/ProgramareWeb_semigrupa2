package demo3;

import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class Worker {
    @Transactional
    public void doSomething() throws Exception {
        if(new Random().nextBoolean()) {
            throw new Exception("Something went wrong.");
        }
    }
}

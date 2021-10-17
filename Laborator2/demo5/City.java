package demo5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class City {
    private String name;
    private Country country;

    public City(@Value("Bucharest") String name, @Autowired @Lazy Country country){
        this.name = name;
        this.country = country;
        System.out.println("City constructor");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", country=" + country.getName() +
                '}';
    }
}

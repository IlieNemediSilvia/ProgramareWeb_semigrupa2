package demo5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class Country {
    private String name;
    private City capital;

    public Country(@Value("Romania") String name, @Autowired @Lazy City capital) {
        this.name = name;
        this.capital = capital;
        System.out.println("Country constructor");
    }

    public String getName() {
        return name;
    }

    public City getCapital() {
        return capital;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", capital='" + capital.getName() + '\'' +
                '}';
    }
}

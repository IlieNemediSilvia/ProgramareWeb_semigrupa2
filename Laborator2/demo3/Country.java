package demo3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {
    private String name;
    private City capital;

    public Country(@Value("Romania") String name, @Autowired @Qualifier("city1") City capital) {
        this.name = name;
        this.capital = capital;
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
                ", capital='" + capital + '\'' +
                '}';
    }
}

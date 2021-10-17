package demo4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {
    private String name;
    private City capital;

    public Country(@Value("Romania") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public City getCapital() {
        return capital;
    }

    @Autowired
    public void setCapital(City capital) {
        this.capital = capital;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", capital='" + capital + '\'' +
                '}';
    }
}

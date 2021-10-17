package demo2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {
    private String name;
    private String capital;
    private String continent;

    public Country(@Value("Romania") String name, @Value("Bucharest") String capital, @Value("Europe") String continent) {
        this.name = name;
        this.capital = capital;
        this.continent = continent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", capital='" + capital + '\'' +
                ", continent='" + continent + '\'' +
                '}';
    }
}

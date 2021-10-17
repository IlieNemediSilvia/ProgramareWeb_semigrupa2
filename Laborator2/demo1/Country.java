package demo1;

public class Country {
    private String name;
    private String capital;
    private String continent;

    public Country(String name, String capital, String continent) {
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

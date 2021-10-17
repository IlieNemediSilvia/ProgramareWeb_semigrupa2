package demo6;

public abstract class Country {
    private String name;
    private Person headOfState;

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getHeadOfState() {
        return headOfState;
    }

    public void setHeadOfState(Person headOfState) {
        this.headOfState = headOfState;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", headOfState=" + headOfState +
                '}';
    }
}

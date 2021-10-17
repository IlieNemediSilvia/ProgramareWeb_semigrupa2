package demo6;

public class King extends Person {
    public King(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return String.format("King %s", getName());
    }
}

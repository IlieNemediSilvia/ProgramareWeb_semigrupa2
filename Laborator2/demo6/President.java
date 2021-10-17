package demo6;

public class President extends Person {

    public President(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return String.format("President %s", getName());
    }
}

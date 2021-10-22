package demo1;

import org.springframework.stereotype.Component;

@Component
public class Person {
    private String firstName;
    private String lastName;
    private char gender;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Regex("[A-Z][a-z]+") String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(@Regex("[A-Z][a-z]+") String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(@Regex("[mMfF]") char gender) {
        this.gender = gender;
    }
}

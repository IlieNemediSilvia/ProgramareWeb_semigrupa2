package demo7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Country {
    @Autowired
    private String largestCity;
    @Autowired
    private String smallestCity;

    @Autowired
    private List<String> cities;

    @Override
    public String toString() {
        return "Country{" +
                "largestCity='" + largestCity + '\'' +
                ", smallestCity='" + smallestCity + '\'' +
                ", cities='" + cities.stream().collect(Collectors.joining(",")) + '\'' +
                '}';
    }
}

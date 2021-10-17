package demo6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Republic extends Country {

    public Republic(@Value("Romania") String name, @Autowired Person president) {
        super(name);
        setHeadOfState(president);
    }
}

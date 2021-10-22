package demo2;

import org.springframework.stereotype.Component;

@Component
@Properties("demo2/settings")
public class Settings {
    private String host;
    private String user;

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }
}

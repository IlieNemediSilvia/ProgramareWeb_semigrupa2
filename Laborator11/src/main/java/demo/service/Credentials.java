package demo.service;

import java.util.Base64;

public class Credentials {
    public static final String AUTHORIZATION = "Authorization";
    private String email;
    private String password;

    public Credentials(String header) {
        if(header.startsWith("Basic ")) {
            header = new String(Base64.getDecoder().decode(header.substring("Basic ".length())));
            if(header.indexOf(':') > 0) {
                email = header.substring(0, header.indexOf(':'));
                password = header.substring(header.indexOf(':') + 1);
            }
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

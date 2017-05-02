package com.team33.model.accounts;

import java.io.Serializable;

public class User implements Serializable {
    private final String user;
    private final String password;
    private final AuthenticationType authenticationType;

    public User(String user, String password, AuthenticationType authenticationType) {
        this.user = user;
        this.password = password;
        this.authenticationType = authenticationType;
    }

    public String getUserName() {
        return user;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public boolean authenticate(String user, String password) {
        if(this.user.equals(user)) {
            if(this.password.equals(password))
                return true;
        }
        return false;
    }
}

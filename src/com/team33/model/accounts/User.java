package com.team33.model.accounts;

public class User {
    private final String user;
    private final String password;

    public User(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public boolean authenticate(String user, String password) {
        if(this.user.equals(user)) {
            if(this.password.equals(password))
                return true;
        }
        return false;
    }
}

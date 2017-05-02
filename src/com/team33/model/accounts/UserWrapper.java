package com.team33.model.accounts;

import java.io.Serializable;
import java.util.HashSet;

public class UserWrapper implements Serializable {

    private HashSet<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
    }

    public boolean userExists(String name) {
        for(User user : users) {
            if(user.getUserName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAuthentication(String userName, String password) {
        for(User user : users) {
            if(user.authenticate(userName, password)) return true;
        }
        return false;
    }

}

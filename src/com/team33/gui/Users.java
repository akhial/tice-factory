package com.team33.gui;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Amine on 25/04/2017.
 */
public class Users implements Serializable {
    HashMap<String,User> users;

    public Users() {
        users = new HashMap<>();
    }
    public void put(String username,User user){
        users.put(username,user);
    }
    public boolean find(String username){
        return users.containsKey(username);
    }
    public User get(String username){
        return users.get(username);
    }
}

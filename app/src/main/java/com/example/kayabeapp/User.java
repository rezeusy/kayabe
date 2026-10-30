package com.example.kayabeapp;

import java.util.List;

public class User {
    public String email, password, name;

    public List<String> trustedContacts;
    public List<String> selected_discounts;



    public User() {
    }


    public User(String email, String password, List<String> selected_discounts, String name, List<String> trustedContacts) {
        this.trustedContacts = trustedContacts;
        this.email = email;
        this.password = password;
        this.selected_discounts = selected_discounts;
        this.name = name;
    }
}

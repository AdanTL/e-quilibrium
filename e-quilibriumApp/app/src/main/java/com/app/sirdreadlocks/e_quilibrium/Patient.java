package com.app.sirdreadlocks.e_quilibrium;

/**
 * Created by Adán on 30/10/2016.
 */

public class Patient {


    private String name;
    private String email;
    private String surname;
    private String phone;


    public Patient() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Patient(String name, String surname, String email, String phone) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }
}

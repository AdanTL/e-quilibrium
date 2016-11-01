package com.app.sirdreadlocks.e_quilibrium;

/**
 * Created by Adán on 30/10/2016.
 */

public class Patient {


    private String name;
    private String email;
    private String surname;
    private String phone;
    private String id;


    public Patient() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Patient(String id, String name, String surname, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.phone = phone;
    }

    public boolean isNull(){
        return (id.equals("") || name.equals("") || email.equals("") || surname.equals("") || phone.equals(""));
    }

    public String getName() { return name; }

    public String getId() { return id; }

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

package com.app.sirdreadlocks.e_quilibrium;

import java.io.Serializable;

/**
 * Created by Adán on 30/10/2016.
 */

public class Patient implements Serializable{


    private String name;
    private String email;
    private String surname;
    private String phone;
    private String id;
    private String birthDate;


    public Patient() {
        // Default constructor required for calls to DataSnapshot.getValue(Patient.class)
    }

    public Patient(String id, String name, String surname, String email, String phone, String birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public boolean isNull(){
        return (id.equals("") || name.equals("") || email.equals("") || surname.equals("") || phone.equals("") || birthDate.equals(""));
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

    public String getBirthDate() {
        return birthDate;
    }
}



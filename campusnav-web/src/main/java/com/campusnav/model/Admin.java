package com.campusnav.model;

/**
 * Demonstrates OOP Inheritance and Polymorphism.
 * Extends the abstract User class and overrides getRole().
 */
public class Admin extends User {
    private String officePhone;

    public Admin() {
        super();
    }

    public Admin(int id, String username, String password, String fullName, String officePhone) {
        super(id, username, password, fullName);
        this.officePhone = officePhone;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }
}

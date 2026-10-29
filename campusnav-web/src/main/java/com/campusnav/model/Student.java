package com.campusnav.model;

/**
 * Demonstrates OOP Inheritance and Polymorphism.
 * Extends the abstract User class and overrides getRole().
 */
public class Student extends User {
    private String department;

    public Student() {
        super();
    }

    public Student(int id, String username, String password, String fullName, String department) {
        super(id, username, password, fullName);
        this.department = department;
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

package com.campusnav.model;

/**
 * Encapsulated domain model representing a physical spot on campus.
 */
public class Location {
    private int id;
    private String name;
    private String building;
    private String floor;
    private String roomNumber;
    private String category;
    private String description;

    public Location() {
    }

    public Location(int id, String name, String building, String floor, String roomNumber, String category, String description) {
        this.id = id;
        this.name = name;
        this.building = building;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " (" + building + ", " + floor + ")";
    }
}

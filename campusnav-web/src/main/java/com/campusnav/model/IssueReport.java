package com.campusnav.model;

/**
 * Model representing a campus maintenance or navigation issue reported by students.
 */
public class IssueReport {
    private int id;
    private String studentUsername;
    private int locationId;
    private String locationName;
    private String category;
    private String description;
    private String status; // PENDING, IN_PROGRESS, RESOLVED
    private String createdAt;

    public IssueReport() {
    }

    public IssueReport(int id, String studentUsername, int locationId, String locationName, String category, String description, String status, String createdAt) {
        this.id = id;
        this.studentUsername = studentUsername;
        this.locationId = locationId;
        this.locationName = locationName;
        this.category = category;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

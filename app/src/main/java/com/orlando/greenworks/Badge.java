package com.orlando.greenworks;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class Badge {
    private String title;
    private String description;
    private String imagePath;
    private int maxPoints;
    private int points;

    public Badge(String title, String imagePath) {
        this.title = title;
        this.imagePath = imagePath;
    }

    public Badge(String title, String imagePath, int maxPoints, int points) {
        this.title = title;
        this.imagePath = imagePath;
        this.maxPoints = maxPoints;
        this.points = points;
    }

    public Badge(String title, String description, String imagePath, int maxPoints, int points) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.maxPoints = maxPoints;
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

package com.orlando.greenworks;

public class ScannedItems {
    private String itemName;
    private String scanDate;
    private int imageResourceId; // Drawable ID for the item image

    // Corrected constructor
    public ScannedItems(String itemName, String scanDate, int imageResourceId) {
        this.itemName = itemName;
        this.scanDate = scanDate;
        this.imageResourceId = imageResourceId;
    }

    // Getters and setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}



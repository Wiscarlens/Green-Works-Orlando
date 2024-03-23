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

public class Item {
    private String itemName;
    private String itemDescription;
    private String itemMaterial;
    private Integer itemPoint;
    private String itemImage;
    private String itemDate;

    public Item(String itemName, String itemDescription, Integer itemPoint, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPoint = itemPoint;
        this.itemImage = itemImage;
    }

    // History constructor
    public Item(String itemImage, String itemName, String itemMaterial, Integer itemPoint, String itemDate) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemMaterial = itemMaterial;
        this.itemPoint = itemPoint;
        this.itemDate = itemDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getItemPoint() {
        return itemPoint;
    }

    public void setItemPoint(Integer itemPoint) {
        this.itemPoint = itemPoint;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public String getItemMaterial() {
        return itemMaterial;
    }

    public void setItemMaterial(String itemMaterial) {
        this.itemMaterial = itemMaterial;
    }
}

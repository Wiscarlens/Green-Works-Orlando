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

import java.io.Serializable;

public class Item implements Serializable {
    private String itemImagePath;
    private String itemName;
    private String itemDescription;
    private String itemMaterial;
    private Integer itemPoint;
    private String itemDate;

    public Item(){

    }

    public Item(String itemImage, String itemName, String itemDescription, String itemMaterial, Integer itemPoint, String itemDate) {
        this.itemImagePath = itemImage;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemMaterial = itemMaterial;
        this.itemPoint = itemPoint;
        this.itemDate = itemDate;
    }

    public Item(String itemName, String itemDescription, Integer itemPoint, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPoint = itemPoint;
        this.itemImagePath = itemImage;
    }

    // History constructor
    public Item(String itemImage, String itemName, String itemMaterial, Integer itemPoint, String itemDate) {
        this.itemImagePath = itemImage;
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

    public String getItemImagePath() {
        return itemImagePath;
    }

    public void setItemImagePath(String itemImagePath) {
        this.itemImagePath = itemImagePath;
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

package com.orlando.orlandorecycle;

public class Item {
    private String itemName;
    private String itemDescription;
    private Integer itemPoint;
    private String itemImage;

    public Item(String itemName, String itemDescription, Integer itemPoint, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPoint = itemPoint;
        this.itemImage = itemImage;
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
}

package com.orlando.greenworks;

public class ScannedItems {
        private String itemName;

    public ScannedItems(String s, String date, int cardboard) {
    }

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

        private String scanDate;
        private int imageResourceId; // Drawable ID for the item image

        // Constructor, getters, and setters
    }


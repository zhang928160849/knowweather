package com.example.remin.knowweather;

/**
 * Created by remin.
 * Created Time 2017/8/24 ${Time}
 */

public class Item {
    private int itemId;

    private String itemName;

    public Item(int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }
}

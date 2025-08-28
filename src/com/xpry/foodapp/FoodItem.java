package com.xpry.foodapp;
//FoodItem.java - Data Model
import java.time.LocalDate;

public class FoodItem {
    private int id;
    private String name;
    private LocalDate expiryDate;

    public FoodItem(int id, String name, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public boolean isExpired() { return expiryDate.isBefore(LocalDate.now()); }
}
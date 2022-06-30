package com.app.beauty.models;

public class SaloonService extends Super {
    String id;
    String title;
    String description;
    String charges;
    String category;

    public SaloonService() {
    }

    public SaloonService(String id, String title, String description, String charges, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.charges = charges;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }
}

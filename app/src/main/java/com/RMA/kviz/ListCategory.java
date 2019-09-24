package com.RMA.kviz;

public class ListCategory {
    private String categoryId;
    private  String category;

    public ListCategory() {
    }

    public ListCategory(String categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

}

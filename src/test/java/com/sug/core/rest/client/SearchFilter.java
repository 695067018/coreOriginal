package com.sug.core.rest.client;

/**
 * Created by greg.chen on 14-10-20.
 */
public class SearchFilter {

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private int categoryId;

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    private String freeText;


    @Override
    public String toString() {
        return "SearchFilter{" +
                "categoryId=" + categoryId +
                '}';
    }

}

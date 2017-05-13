package com.td.innovate.tdiscount.model;

/**
 * Created by helenpark on 2015-11-17.
 */
public class Payment {
    private String title;
    private String description;
    private String type;

    public Payment(String title, String description, String type) {
        this.title = title;
        this.description = description;
        this.type = type;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

}


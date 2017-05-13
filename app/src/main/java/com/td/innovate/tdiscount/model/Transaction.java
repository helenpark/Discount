package com.td.innovate.tdiscount.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mmmoussa on 2015-11-10.
 */

//TODO: add location field to all dunny data
    public class Transaction implements Serializable {
    private String name = "";
    private String stringDate = "";
    private Date date;
    private Double cost = 0.0;
    private String id = "";

    public Transaction(String name, String stringDate, Double cost, String id) {
        this.name = name;
        this.stringDate = stringDate;
        this.id = id;
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = date;
        this.cost = cost;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getStringDate() {
        return stringDate;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "name: " + name +"\n"
                +"cost: " + cost +"\n";
    }

    public Double getCost() {
        return cost;
    }

    public String getid() {
        return id;
    }
}

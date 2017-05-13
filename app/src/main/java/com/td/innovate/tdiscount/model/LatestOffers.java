package com.td.innovate.tdiscount.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zunairsyed on 2015-11-10.
 */
public class LatestOffers {

    private String shipping;
    private String price;
    private String currency;
    private String availability;
    private String seller;


    public LatestOffers(String jsonAsString) throws JSONException {
        this(new JSONObject(jsonAsString));
    }

    public LatestOffers(String a, String b){
        this.seller = a;
        this.price = b;
    }

    public LatestOffers(JSONObject jsonObject) throws JSONException {
        this.shipping = (jsonObject.has("shipping")) ? jsonObject.get("shipping").toString() : "null";
        this.currency = (jsonObject.has("currency")) ? jsonObject.get("currency").toString() : "null";
        this.availability = (jsonObject.has("availability")) ? jsonObject.get("availability").toString() : "null";
        this.seller = (jsonObject.has("seller")) ? jsonObject.get("seller").toString() : "null";
        this.price = (jsonObject.has("price")) ? jsonObject.get("price").toString() : "null";
    }

    public String getPrice() {
        return price;
    }

    public String getAvailability() {
        return availability;
    }


    public String getCurrency() {
        return currency;
    }

    public String getSeller() {
        return seller;
    }

    public String getShipping() {
        return shipping;
    }

    @Override
    public String toString() {
        return "seller: " + seller
                + "\navailbility: " + availability
                + "\nprice: $" + price + " " + currency;
    }
}

package com.td.innovate.tdiscount.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zunairsyed on 2015-11-04.
 */
public class  Offer {

    private String shop_id;
    private String shop_name;
    private String shop_url;
    private String price;
    private String name;
    private String price_with_shipping;
    private String shipping_costs;
    private String currency;
    private String url;
    private boolean shouldBeShown = true;

    public Offer(String jsonAsString) throws JSONException {
        this(new JSONObject(jsonAsString));
    }

    public Offer(JSONObject jsonObject) throws JSONException {



        this.shop_id = (jsonObject.has("shop_id")) ? jsonObject.get("shop_id").toString() : "null";
        this.shop_name = (jsonObject.has("shop_name")) ? jsonObject.get("shop_name").toString() : "null";
        this.shop_url = (jsonObject.has("shop_url")) ? jsonObject.get("shop_url").toString() : "null";
        this.currency = (jsonObject.has("currency")) ? jsonObject.get("currency").toString() : "null";
        this.url = (jsonObject.has("url")) ? jsonObject.get("url").toString() : "null";
        this.name = (jsonObject.has("name")) ? jsonObject.get("name").toString() : "null";

        if(jsonObject.has("price")) {
            if (!jsonObject.get("price").toString().equals("null")) {
                this.price = jsonObject.get("price").toString();
            } else {
                shouldBeShown = false;
            }
        }else{
            shouldBeShown = false;
        }

        this.price_with_shipping = (jsonObject.has("price_with_shipping")) ? jsonObject.get("price_with_shipping").toString() : "null";
        this.shipping_costs = (jsonObject.has("shipping_costs")) ? jsonObject.get("shipping_costs").toString() : "null";
    }

    @Override
    public String toString() {
        return "shop_name: " + shop_name
                + "\nItem: " + name
                + "\nPrice: $" + price + " " + currency;
    }


    public boolean getShouldBeShown() {
        return shouldBeShown;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getShop_url() {
        return shop_url;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getPrice_with_shipping() {
        return price_with_shipping;
    }

    public String getShipping_costs() {
        return shipping_costs;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUrl() {
        return url;
    }

    public void setShouldBeShown(boolean shouldBeShown) {
        this.shouldBeShown = shouldBeShown;
    }
}



package com.td.innovate.tdiscount.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zunairsyed on 2015-11-05.
 */
public class Product {

    private String source;
    private String country;
    private String key;
    private String value;
    private String success;
    private String name;
    private String brand_name;
    private String category_name;
    private String review_count;
    private String review_rating;
    private String url;
    private String image_url;
    private String description;
    private String reason;
    private String barcode;


    private boolean couldNotFindProduct = false;
    private ArrayList<Offer> offers;

    public Product(String jsonAsString) throws JSONException {
        this(new JSONObject(jsonAsString));
    }

    public Product(JSONObject jsonObject) throws JSONException {
        offers = new ArrayList<Offer>();

        this.reason = (jsonObject.has("reason")) ? jsonObject.get("reason").toString() : "null";
        this.success = (jsonObject.has("success")) ? jsonObject.get("success").toString() : "null";
        if (!reason.equals("not found") && !success.equals("false")) {

            this.source = (jsonObject.has("source")) ? jsonObject.get("source").toString() : "null";
            this.country = (jsonObject.has("country")) ? jsonObject.get("country").toString() : "null";
            this.key = (jsonObject.has("key")) ? jsonObject.get("key").toString() : "null";
            this.value = (jsonObject.has("value")) ? jsonObject.get("value").toString() : "null";

            this.name = (jsonObject.has("name")) ? jsonObject.get("name").toString() : "null";
            this.brand_name = (jsonObject.has("brand_name")) ? jsonObject.get("brand_name").toString() : "null";
            this.category_name = (jsonObject.has("category_name")) ? jsonObject.get("category_name").toString() : "null";
            this.review_count = (jsonObject.has("review_count")) ? jsonObject.get("review_count").toString() : "null";
            this.review_rating = (jsonObject.has("review_rating")) ? jsonObject.get("review_rating").toString() : "null";
            this.url = (jsonObject.has("url")) ? jsonObject.get("url").toString() : "null";
            this.image_url = (jsonObject.has("image_url")) ? jsonObject.get("image_url").toString() : "null";
            this.description = (jsonObject.has("description")) ? jsonObject.get("description").toString() : "null";

            try {
                this.barcode = (jsonObject.has("gtins")) ? jsonObject.getJSONArray("gtins").get(0).toString() : "null";
            }catch (Exception e){
                e.printStackTrace();
                Log.d("NO BARCODE FOUND", "No barcode found");
            }

            try {
                for (int i = 0; i < jsonObject.getJSONArray("offers").length(); i++) {
                    offers.add(new Offer(jsonObject.getJSONArray("offers").get(i).toString()));
                }
            } catch (Exception e) {
                Log.d("ERROR PARSING", "OOPS, Either Offers were not available, or parsing went wrong");
            }

        }else{
            couldNotFindProduct = true;
        }


    }

    @Override
    public String toString() {
        return "name: " + name
                + "\nbrand_name: " + brand_name
                + "\ndescription: $" + description;

    }


    public String getSource() {
        return source;
    }

    public String getCountry() {
        return country;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getReview_count() {
        return review_count;
    }

    public String getReview_rating() {
        return review_rating;
    }

    public String getSuccess() {
        return success;
    }

    public String getUrl() {
        return url;
    }

    public String getValue() {
        return value;
    }

    public boolean couldNotFindProduct() {
        return couldNotFindProduct;
    }

    public void setCouldNotFindProduct(boolean couldNotFindProduct) {
        this.couldNotFindProduct = couldNotFindProduct;
    }

    public String getBarcode() {
        return barcode;
    }


}


//
//"source": "google-shopping",
//        "country": "us",
//        "key": "keyword",
//        "value": "xbox",
//        "success": true,
//        "updated_at": "2015-11-05T15:46:53Z",
//        "id": "loose-offers-keyword-xbox",
//        "condition_filter": "all",
//        "gtins": null,
//        "eans": null,
//        "name": null,
//        "brand_name": null,
//        "category_name": null,
//        "review_count": null,
//        "review_rating": null,
//        "url": "https://www.google.com/search?tbm=shop&tbs=vw:l,p_ord:p&q=xbox",
//        "image_url": null,
//        "description": null,
//        "offers"

package com.appzone.dukkan.models;

import java.io.Serializable;

public class OrderItem implements Serializable
{
    private String product_id;
    private String feature_id;
    private String product_price_id;
    private int product_quantity;
    private int product_price;
    private int product_total_price;

    public OrderItem() {
    }

    public OrderItem(String product_id, String feature_id, String product_price_id, int product_quantity, int product_price, int product_total_price) {
        this.product_id = product_id;
        this.feature_id = feature_id;
        this.product_price_id = product_price_id;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.product_total_price = product_total_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(String feature_id) {
        this.feature_id = feature_id;
    }

    public String getProduct_price_id() {
        return product_price_id;
    }

    public void setProduct_price_id(String product_price_id) {
        this.product_price_id = product_price_id;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(int product_total_price) {
        this.product_total_price = product_total_price;
    }
}

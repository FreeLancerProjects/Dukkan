package com.appzone.dukkan.models;

import java.io.Serializable;
import java.util.List;

public class OrderToUploadModel implements Serializable {

    private String client_id;
    private String client_name;
    private String client_Phone;
    private String client_address;
    private double lat;
    private double lng;
    private String client_street;
    private String notes;
    private String time_type;
    private String coupon_code;
    private String coupon_value;
    private String payment_method;
    private double order_total_price;
    private List<OrderItem> orderItemList;

    public OrderToUploadModel() {
    }


    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_Phone() {
        return client_Phone;
    }

    public void setClient_Phone(String client_Phone) {
        this.client_Phone = client_Phone;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getClient_street() {
        return client_street;
    }

    public void setClient_street(String client_street) {
        this.client_street = client_street;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTime_type() {
        return time_type;
    }

    public void setTime_type(String time_type) {
        this.time_type = time_type;
    }

    public String getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        this.coupon_value = coupon_value;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public double getOrder_total_price() {
        return order_total_price;
    }

    public void setOrder_total_price(double order_total_price) {
        this.order_total_price = order_total_price;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}

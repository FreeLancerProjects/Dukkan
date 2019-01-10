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
    private double date;
    private double time;
    private String payment_method;
    private int order_total_price;
    private List<OrderItem> orderItemList;

    public OrderToUploadModel() {
    }

    public OrderToUploadModel(String client_id, String client_name, String client_Phone, String client_address, double lat, double lng, String client_street, String notes, double date, double time, String payment_method, int order_total_price, List<OrderItem> orderItemList) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_Phone = client_Phone;
        this.client_address = client_address;
        this.lat = lat;
        this.lng = lng;
        this.client_street = client_street;
        this.notes = notes;
        this.date = date;
        this.time = time;
        this.payment_method = payment_method;
        this.order_total_price = order_total_price;
        this.orderItemList = orderItemList;
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

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public int getOrder_total_price() {
        return order_total_price;
    }

    public void setOrder_total_price(int order_total_price) {
        this.order_total_price = order_total_price;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}

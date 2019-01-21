package com.appzone.dukkan.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrdersModel implements Serializable{

    private List<Order> data;

    public List<Order> getData() {
        return data;
    }

    public class Order implements Serializable
    {
        private int id;
        private double lat;
        @SerializedName("long")
        private double lng;
        private String address;
        private String street;
        private String note;
        private int total;
        private int coupon_value;
        private String coupon_code;
        private int time_type;
        private int payment_method;
        private int status;
        private String created_at;
        private Client client;
        private Delegate delegate;
        private List<Products> products;

        public int getId() {
            return id;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public String getAddress() {
            return address;
        }

        public String getStreet() {
            return street;
        }

        public String getNote() {
            return note;
        }

        public int getTotal() {
            return total;
        }

        public int getCoupon_value() {
            return coupon_value;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public int getTime_type() {
            return time_type;
        }

        public int getPayment_method() {
            return payment_method;
        }

        public int getStatus() {
            return status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public Client getClient() {
            return client;
        }

        public Delegate getDelegate() {
            return delegate;
        }

        public List<Products> getProducts() {
            return products;
        }
    }

    public class Client implements Serializable
    {
        private int id;
        private String name;
        private String phone;
        private double rate;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public double getRate() {
            return rate;
        }
    }

    public class Delegate implements Serializable
    {
        private int id;
        private String name;
        private String phone;
        private String avatar;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getAvatar() {
            return avatar;
        }
    }

    private class Products implements Serializable
    {

        private int feature_id;
        private int quantity;
        private int total;
        private Product_Price product_price;
        private Product product;
        private Feature feature;

        public int getFeature_id() {
            return feature_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getTotal() {
            return total;
        }

        public Product_Price getProduct_price() {
            return product_price;
        }

        public Product getProduct() {
            return product;
        }

        public Feature getFeature() {
            return feature;
        }
    }

    public class Product_Price implements Serializable
    {
        private int id;
        private int net_price;
        private String size_ar;
        private String size_en;

        public int getId() {
            return id;
        }

        public int getNet_price() {
            return net_price;
        }

        public String getSize_ar() {
            return size_ar;
        }

        public String getSize_en() {
            return size_en;
        }
    }

    public class Product implements Serializable
    {
        private int id;
        private String name_ar;
        private String name_en;
        private List<String>image;
        private String main_category_id;
        private String sub_category_id;

        public int getId() {
            return id;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public List<String> getImage() {
            return image;
        }

        public String getMain_category_id() {
            return main_category_id;
        }

        public String getSub_category_id() {
            return sub_category_id;
        }
    }

    public class Feature implements Serializable
    {
        private int feature_id;
        private int discount;

        public int getFeature_id() {
            return feature_id;
        }

        public int getDiscount() {
            return discount;
        }
    }
}

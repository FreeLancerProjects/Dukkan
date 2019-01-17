package com.appzone.dukkan.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CouponModel implements Serializable {
    private int code;
    @SerializedName("coupon")
    private Coupon coupon;

    public int getCode() {
        return code;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public class Coupon implements Serializable
    {
        private String value;
        private String code;
        private String active;

        public String getValue() {
            return value;
        }

        public String getCode() {
            return code;
        }

        public String getActive() {
            return active;
        }
    }
}

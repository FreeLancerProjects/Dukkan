package com.appzone.dukkan.models;

import java.io.Serializable;
import java.util.List;

public class MainCategory implements Serializable {

    private List<MainCategoryItems>  data;

    public List<MainCategoryItems> getData() {
        return data;
    }

    public class MainCategoryItems implements Serializable
    {
        private String id;
        private String name_ar;
        private String name_en;
        private String image;
        private List<SubCategory> sub_categories;

        public String getId() {
            return id;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public String getImage() {
            return image;
        }

        public List<SubCategory> getSub_categories() {
            return sub_categories;
        }
    }

    public class SubCategory implements Serializable
    {
        private String id;
        private String name_ar;
        private String name_en;
        private String main_category_id;
        private List<Products> products;


        public String getId() {
            return id;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public String getMain_category_id() {
            return main_category_id;
        }

        public List<Products> getProducts() {
            return products;
        }
    }

    public class Products implements Serializable
    {
        private String id;
        private String name_ar;
        private String name_en;
        private List<String> image;
        private List<Prices_Sizes>  size_prices;
        private List<Features> features;

        public String getId() {
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

        public List<Prices_Sizes> getSize_prices() {
            return size_prices;
        }

        public List<Features> getFeatures() {
            return features;
        }
    }

    public class Prices_Sizes implements Serializable
    {
        private String id;
        private String net_price;
        private String size_ar;
        private String size_en;

        public String getId() {
            return id;
        }

        public String getNet_price() {
            return net_price;
        }

        public String getSize_ar() {
            return size_ar;
        }

        public String getSize_en() {
            return size_en;
        }
    }

    public class Features implements Serializable
    {
        private String discount;
        private OldPrice old_price;

        public String getDiscount() {
            return discount;
        }

        public OldPrice getOld_price() {
            return old_price;
        }
    }

    public class OldPrice implements Serializable
    {
        private String id;
        private String net_price;

        public String getId() {
            return id;
        }

        public String getNet_price() {
            return net_price;
        }
    }
}

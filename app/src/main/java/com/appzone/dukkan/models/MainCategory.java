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
    }
}

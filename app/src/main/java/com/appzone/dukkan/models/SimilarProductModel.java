package com.appzone.dukkan.models;

import java.io.Serializable;
import java.util.List;

public class SimilarProductModel implements Serializable {
    List<MainCategory.Products> products;

    public List<MainCategory.Products> getProducts() {
        return products;
    }
}

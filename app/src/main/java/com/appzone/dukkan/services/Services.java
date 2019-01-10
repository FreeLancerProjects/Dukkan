package com.appzone.dukkan.services;

import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.models.ProductPaginationModel;
import com.appzone.dukkan.models.SimilarProductModel;
import com.appzone.dukkan.models.TaxModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services {

    @GET("api/main-categories")
    Call<MainCategory> getMainCategory();

    @GET("api/sub-category/{sub_category_id}")
    Call<ProductPaginationModel> getProductPagination(@Path("sub_category_id") String sub_category_id, @Query("page") int page_index);

    @GET("api/get-tax")
    Call<TaxModel> getTax();

    @GET("api/featured-products")
    Call<ProductPaginationModel> getOfferedProductPagination(@Query("page") int page_index);

    @GET("api/similar")
    Call<SimilarProductModel> getSimilarProducts(@Query("product_id") String product_id,
                                                 @Query("main_category_id") String main_category_id,
                                                 @Query("sub_category_id") String sub_category_id
                                                 );

}

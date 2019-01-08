package com.appzone.dukkan.services;

import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.models.ProductPaginationModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services {

    @GET("api/main-categories")
    Call<MainCategory> getMainCategory();

    @GET("api/sub-category/{sub_category_id}")
    Call<ProductPaginationModel> getProductPaganation(@Path("sub_category_id") String sub_category_id,@Query("page") int page_index);
}

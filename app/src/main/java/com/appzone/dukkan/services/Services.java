package com.appzone.dukkan.services;

import com.appzone.dukkan.models.CouponModel;
import com.appzone.dukkan.models.DeliveryCostModel;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.models.ProductPaginationModel;
import com.appzone.dukkan.models.ResponseModel;
import com.appzone.dukkan.models.SimilarProductModel;
import com.appzone.dukkan.models.TaxModel;
import com.appzone.dukkan.models.Terms_Condition_Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("api/get-terms-and-conditions")
    Call<Terms_Condition_Model> getTermsConditions();

    @FormUrlEncoded
    @POST("api/send-contact")
    Call<ResponseModel> sendContactUs(@Field("name") String name,
                                      @Field("phone") String phone,
                                      @Field("message") String message
                                      );

    @FormUrlEncoded
    @POST("api/get-recent-search")
    Call<ProductPaginationModel> getRecentSearchProducts(@Field("ids[]") List<String> ids);

    @GET("api/search-products")
    Call<ProductPaginationModel> search(@Query("q") String query,@Query("page") int page_index);

    @GET("api/get-delivery-cost")
    Call<DeliveryCostModel> getDeliveryCost();

    @GET("api/coupons/{coupon_id}")
    Call<CouponModel> isCouponAvailable(@Path("coupon_id") String coupon_id);
}

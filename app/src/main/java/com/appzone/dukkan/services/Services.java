package com.appzone.dukkan.services;

import com.appzone.dukkan.models.MainCategory;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Services {

    @GET("api/main-categories")
    Call<MainCategory> getMainCategory();
}

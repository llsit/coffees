package com.cmu.nuts.coffee9.main.retrofit;

import com.cmu.nuts.coffee9.model.Shop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DistanceService {
    @POST("cal")
    Call<List<Shop>> NearbyShop(@Query("lat") Double lat, @Query("lng") Double lng);
}

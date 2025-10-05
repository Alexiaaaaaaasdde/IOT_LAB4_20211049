package com.example.iot_lab4_20211049.net;

import com.example.iot_lab4_20211049.search.LocationItem;
import com.example.iot_lab4_20211049.forecast.ForecastResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("search.json")
    Call<List<LocationItem>> search(@Query("key") String key, @Query("q") String query);

    @GET("forecast.json")
    Call<ForecastResponse> forecast(@Query("key") String key,
                                    @Query("q") String q,        // "id:xxxxx"
                                    @Query("days") int days);    // 14
}

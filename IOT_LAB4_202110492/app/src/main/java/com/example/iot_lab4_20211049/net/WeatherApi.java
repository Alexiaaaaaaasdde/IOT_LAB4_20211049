package com.example.iot_lab4_20211049.net;

import com.example.iot_lab4_20211049.search.LocationItem;
import com.example.iot_lab4_20211049.forecast.ForecastResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//Se hace uso de LLM para entender como usar las API_KEY de OpenWeatherMap
public interface WeatherApi {

    //Busqueda de locations
    @GET("search.json")
    Call<List<LocationItem>> search(
            @Query("key") String key,
            @Query("q") String query
    );

    @GET("forecast.json")
    Call<ForecastResponse> forecast(
            @Query("key") String key,
            @Query("q") String q,      // Ej: "id:1794357"
            @Query("days") int days    // 2semanas
    );

    //Pronóstico por fecha específica (hoy .. +14)
    @GET("forecast.json")
    Call<ForecastResponse> forecastByDate(
            @Query("key") String key,
            @Query("q") String q,        // "id:1794357"
            @Query("dt") String dateIso  // "YYYY-MM-DD"
    );

    //History por fecha específica (hasta -365 días)
    @GET("history.json")
    Call<ForecastResponse> historyByDate(
            @Query("key") String key,
            @Query("q") String q,
            @Query("dt") String dateIso
    );

    //Futuro por fecha específica (+15 .. +300 días)
    @GET("future.json")
    Call<ForecastResponse> futureByDate(
            @Query("key") String key,
            @Query("q") String q,
            @Query("dt") String dateIso
    );
}

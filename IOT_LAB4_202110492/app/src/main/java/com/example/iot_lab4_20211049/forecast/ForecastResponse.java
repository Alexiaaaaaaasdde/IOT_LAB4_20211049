package com.example.iot_lab4_20211049.forecast;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {

    public Location location;     // info del lugar (opcional para lo que usamos)
    public Forecast forecast;     // bloque con days -> hours

    // ---- objetos raíz ----
    public static class Location {
        public String name;
        public String region;
        public String country;
        public double lat;
        public double lon;
        @SerializedName("tz_id") public String tzId;
    }

    public static class Forecast {
        public List<ForecastDay> forecastday;
    }

    // ---- día ----
    public static class ForecastDay {
        public String date;
        @SerializedName("date_epoch") public long dateEpoch;
        public Day day;
        public Astro astro;
        public List<Hour> hour;
    }

    // Datos agregados/diarios (para tu lista de 14 días)
    public static class Day {
        @SerializedName("maxtemp_c") public double maxtempC;
        @SerializedName("mintemp_c") public double mintempC;
        @SerializedName("avgtemp_c") public double avgtempC;

        @SerializedName("maxwind_kph") public double maxwindKph;
        @SerializedName("totalprecip_mm") public double totalprecipMm;
        @SerializedName("avghumidity") public double avghumidity;

        public Condition condition;
        public double uv;
    }

    public static class Astro {
        public String sunrise;
        public String sunset;
        public String moonrise;
        public String moonset;
        @SerializedName("moon_phase") public String moonPhase;
        @SerializedName("moon_illumination") public String moonIllumination;
    }

    public static class Condition {
        public String text;
        public String icon;   // viene con //cdn..., le agregamos https: en el adapter
        public int code;
    }

    // ---- hora a hora (lo que te falta) ----
    public static class Hour {
        public String time;               // "YYYY-MM-DD HH:00"
        @SerializedName("temp_c") public double temp_c;
        public int humidity;

        @SerializedName("chance_of_rain") public int chance_of_rain;
        @SerializedName("precip_mm") public double precip_mm;

        public Condition condition;

        // algunos extras útiles (opcional)
        @SerializedName("wind_kph") public double wind_kph;
        @SerializedName("feelslike_c") public double feelslike_c;
        public int is_day;
    }
}

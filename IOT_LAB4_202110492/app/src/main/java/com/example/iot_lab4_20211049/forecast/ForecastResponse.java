package com.example.iot_lab4_20211049.forecast;

import java.util.List;

public class ForecastResponse {
    public Location location;
    public Forecast forecast;

    public static class Location { public String name, region, country; public double lat, lon; public String tz_id; }
    public static class Forecast { public List<ForecastDay> forecastday; }
    public static class ForecastDay {
        public String date; public Day day;
        public static class Day { public double maxtemp_c, mintemp_c; public Condition condition; }
    }
    public static class Condition { public String text; public String icon; }
}

package com.example.remin.knowweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by remin.
 * Created Time 2017/7/16 ${Time}
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @SerializedName("hourly_forecast")
    public List<HourlyForecast> hourlyForecastList;

}

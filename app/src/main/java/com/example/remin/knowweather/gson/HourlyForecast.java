package com.example.remin.knowweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by remin.
 * Created Time 2017/9/17 ${Time}
 */

public class HourlyForecast {
    public String date;

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("wind")
    public Wind wind;

    public class Wind{
        @SerializedName("dir")
        public String direction;
        @SerializedName("sc")
        public String rank;
    }
}

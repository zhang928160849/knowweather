package com.example.remin.knowweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by remin.
 * Created Time 2017/7/16 ${Time}
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}

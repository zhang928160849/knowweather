package com.example.remin.knowweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by remin.
 * Created Time 2017/7/16 ${Time}
 */

public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}

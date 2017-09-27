package com.example.remin.knowweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by remin.
 * Created Time 2017/7/16 ${Time}
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}

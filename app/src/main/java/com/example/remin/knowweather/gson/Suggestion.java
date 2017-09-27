package com.example.remin.knowweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by remin.
 * Created Time 2017/7/16 ${Time}
 */


public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;
    public class Comfort{
        public String brf;
        @SerializedName("txt")
        public String info;
    }
    public class CarWash{
        public String brf;
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        public String brf;
        @SerializedName("txt")
        public String info;


    }

}

package com.example.remin.knowweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by remin.
 * Created Time 2017/7/11 ${Time}
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;
    private int isSelected;

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setSelected(int selected) {isSelected = selected;}

    public int getIsSelected() {return isSelected;}
}

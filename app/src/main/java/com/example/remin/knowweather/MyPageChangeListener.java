package com.example.remin.knowweather;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.remin.knowweather.db.City;
import com.example.remin.knowweather.db.County;
import com.example.remin.knowweather.view.CityPointView;

import java.util.ArrayList;

/**
 * Created by remin.
 * Created Time 2017/9/10 ${Time}
 */

public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
    /*
    * 导航点的最大长度
    * */
    private float pointviewLength ;
    /*
    * 传给CityPointView的点长度数组
    * */
    private float pointLength[] = new float[4];
    /*
    * 需要展示天气的区域数量
    * */
    private int countyNum;
    private Context context;
    private TextView cityTitle;
    CityPointView cityPointView;
    ArrayList<County> countyList = new ArrayList<>();


    public MyPageChangeListener(CityPointView cityPointView, TextView cityTitle, ArrayList<County> countyList, Context context) {
        this.cityPointView = cityPointView;
        this.countyList = countyList;
        this.context = context;
        this.cityTitle = cityTitle;
        countyNum = countyList.size();
        //测试需要删去
        if(countyNum>=4){
            countyNum = 4;
        }
    }

    public void updateDate(ArrayList<County> countyList){
        this.countyList = countyList;
        countyNum = countyList.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        //        计算出选中的页面的导航点的最大长度为屏幕宽度的十分之一
        pointviewLength = getScreenWidth()/10;
        for(int i=0;i<countyNum;i++){
            if (i==position){
                pointLength[i]=pointviewLength;
            }else{
                pointLength[i]=20;
            }
        }
//
        if(positionOffset>0.8f){
            positionOffset = 0.8f;
        }
        pointLength[position] = pointLength[position] - positionOffset*pointviewLength;
        pointLength[position+1] = pointLength[position+1]+positionOffset*pointviewLength;
        cityPointView.updateView(pointLength);
        if (positionOffset == 0){
            cityTitle.setText(countyList.get(position).getCountyName());
        }
    }
    /*
    * 当页面滑动完毕后对cityPointView控件的圆点显示进行刷新
    * */
    @Override
    public void onPageSelected(int position) {
        try{
            cityPointView.currentPosition = position;
            cityTitle.setText(countyList.get(position).getCountyName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public float getScreenWidth(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }
}

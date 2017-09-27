package com.example.remin.knowweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.remin.knowweather.adapter.MyViewPagerAdapter;
import com.example.remin.knowweather.adapter.RecyclerViewAdapter;
import com.example.remin.knowweather.adapter.TestStackAdapter;
import com.example.remin.knowweather.db.City;
import com.example.remin.knowweather.db.County;
import com.example.remin.knowweather.gson.Weather;
import com.example.remin.knowweather.util.HttpUtil;
import com.example.remin.knowweather.util.Utility;
import com.example.remin.knowweather.view.CityPointView;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements CardStackView.ItemExpendListener{

    private static final String TAG = "MainActivity";
    private static boolean isSelected = true;
    private int requestCode;
    /*
    * 需要展示的区域的列表
    * */
    private List<County> countyList;
    /*
    * 需要展示区域的天气信息
    * */
    private ArrayList<Weather> weathers = new ArrayList<>();
    private ArrayList<Weather> weathersReponse = new ArrayList<>();
    /*
    * 管理每页的集合
    * */
    private ArrayList<View> pageViews;
    /*
    * 初始化cardstack的颜色数组
    * */
    public static Integer[] COLOR_DATAS = new Integer[]{
            R.color.halfTransparent,
            R.color.halfTransparent,
            R.color.halfTransparent,
            R.color.halfTransparent,
            R.color.halfTransparent,
            R.color.halfTransparent,
    };

    /*
    * 声明cardstack各个控件
    * */
    private CardStackView mCardStackView;
    private TestStackAdapter mStackAdapter;
    private ArrayList<TestStackAdapter> mStackAdapterList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefresh;
    private MyViewPagerAdapter mMyViewPagerAdapter;
    private ImageView bingBackgroundView;
    private CityPointView cityPointView;
    private TextView cityTitle;
    private Button navButton;


    /*
    *
    * */
    private MyPageChangeListener pageChangeListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initialData();
        initialView();
        /*
        * 数据持久化操作，读sharedpreference
        * */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = preferences.getString("background_pic",null);
        if(bingPic != null){
            Glide.with(this).load(bingPic).into(bingBackgroundView);
        }else{
            loadBingPic();
        }

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this,CitiesActivity.class);
                requestCode = 1;
                startActivityForResult(intent,requestCode);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //从数据库中判断需要展示天气的区域
    private void initialData(){
        Weather weather = new Weather();
        String mWeatherId;
        countyList = DataSupport.where("isselected = ?","1").find(County.class);
        if(countyList != null){
            weathers.clear();
            for(County county:countyList){
                //遍历county对象进行
                mWeatherId = county.getWeatherId();
                requestWeather(mWeatherId);
                }
        }
    }

    private void updateData(){
        Weather weather = new Weather();
        String mWeatherId;
        LayoutInflater inflater = getLayoutInflater();
        countyList = DataSupport.where("isselected = ?","1").find(County.class);
        if(countyList != null){
            weathers.clear();
            mStackAdapterList.clear();
            weathersReponse.clear();
            for(County county:countyList){
                //遍历county对象进行
                mWeatherId = county.getWeatherId();
                requestWeather(mWeatherId);
            }
            pageViews.clear();
            for(int i = 0;i<countyList.size();i++){
                View viewWeather1 = inflater.inflate(R.layout.single_viewpage,null);
                pageViews.add(viewWeather1);
            }
            for (int i = 0;i<pageViews.size();i++){
                mCardStackView = (CardStackView)pageViews.get(i).findViewById(R.id.stack_view);
                mCardStackView.setItemExpendListener(this);
                mStackAdapter = new TestStackAdapter(this,weathers,i,cityTitle,(ArrayList<County>) countyList);
                mCardStackView.setAdapter(mStackAdapter);
                mStackAdapter.setData(Arrays.asList(COLOR_DATAS));
                mCardStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(mCardStackView));
                mStackAdapterList.add(mStackAdapter);
            }
            cityPointView.initPaint((ArrayList<County>)countyList);
            cityPointView.invalidate();

            pageChangeListener.updateDate((ArrayList<County>)countyList);
            mMyViewPagerAdapter.notifyDataSetChanged();

        }
    }

    private void initialView(){
        LayoutInflater inflater = getLayoutInflater();
        bingBackgroundView = (ImageView)findViewById(R.id.weather_background);
        cityPointView = (CityPointView)findViewById(R.id.citypoints_view);
        navButton = (Button)findViewById(R.id.nav_button);
        cityTitle = (TextView)findViewById(R.id.title_city);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setRefreshing(true);
        cityPointView.initPaint((ArrayList<County>)countyList);
        pageViews = new ArrayList<>();
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager1);
        for(int i = 0;i<countyList.size();i++){
            View viewWeather1 = inflater.inflate(R.layout.single_viewpage,null);
            pageViews.add(viewWeather1);
        }
//        View viewWeather2 = inflater.inflate(R.layout.single_viewpage,null);
//        pageViews.add(viewWeather2);
//        View viewWeather3 = inflater.inflate(R.layout.single_viewpage,null);
//        pageViews.add(viewWeather3);

        /*
        * 循环，对与viewpage的每一页的cardstackView进行控件的初始化
        * */
        for (int i = 0;i<pageViews.size();i++){
            mCardStackView = (CardStackView)pageViews.get(i).findViewById(R.id.stack_view);
            mCardStackView.setItemExpendListener(this);
            mStackAdapter = new TestStackAdapter(this,weathers,i,cityTitle,(ArrayList<County>) countyList);
            mCardStackView.setAdapter(mStackAdapter);
            mStackAdapter.setData(Arrays.asList(COLOR_DATAS));
            mCardStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(mCardStackView));
            mStackAdapterList.add(mStackAdapter);
//                new Handler().postDelayed(
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                mStackAdapter.updateData(Arrays.asList(COLOR_DATAS));
//                            }
//                        }
//                        ,200);

        }
        mMyViewPagerAdapter = new MyViewPagerAdapter();
        mMyViewPagerAdapter.setViews(pageViews);
        viewPager.setAdapter(mMyViewPagerAdapter);
        pageChangeListener = new MyPageChangeListener(cityPointView,cityTitle,
                (ArrayList<County>) countyList,this);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }
    /*
    * CardStackView.ItemExpendListener{
    * */

    @Override
    public void onItemExpend(boolean expend) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                swipeRefresh.setRefreshing(true);
                updateData();
                break;
        }
    }

    /*
        * 加载背景图片
        * */
    private void loadBingPic(){
        final String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor =
                        PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("background_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingBackgroundView);
                    }
                });
            }
        });
    }

    private void requestWeather(final String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        /*
        * 接口回调天气信息
        * */
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //如果请求返回信息正确，储存并
                        if(weather != null && weather.status.equals("ok")){
                            weathersReponse.add(weather);
                            if(mStackAdapterList != null && weathersReponse.size() == countyList.size()){
                                for(County county:countyList){
                                    for(Weather weather:weathersReponse){
                                        if(county.getWeatherId().equals(weather.basic.weatherId)){
                                            weathers.add(weather);
                                            break;
                                        }
                                    }
                                }

                                swipeRefresh.setRefreshing(false);
                                for(TestStackAdapter adapter:mStackAdapterList){
                                    try{
                                        adapter.changeWeather(weathers);
                                        adapter.notifyDataSetChanged();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString(String.valueOf(weather.basic.weatherId),responseText);
                            editor.apply();
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

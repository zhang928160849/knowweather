package com.example.remin.knowweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remin.knowweather.adapter.MyViewPagerAdapter;
import com.example.remin.knowweather.adapter.RecyclerViewAdapter;
import com.example.remin.knowweather.adapter.TestStackAdapter;
import com.example.remin.knowweather.db.County;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;

public class MainActivity extends AppCompatActivity {

    private List<County> counties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        counties = DataSupport.where("isSelected = ?","1").find(County.class);
        //从数据库中判断是否有被添加的城市，如有直接查询天气
        if(!counties.isEmpty()){
            if(counties.get(0) != null){
                Intent intent = new Intent(this,WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}

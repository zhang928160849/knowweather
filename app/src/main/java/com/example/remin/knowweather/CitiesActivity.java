package com.example.remin.knowweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.example.remin.knowweather.adapter.RecyclerViewAdapter;
import com.example.remin.knowweather.db.County;
import com.example.remin.knowweather.gson.Weather;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CitiesActivity extends AppCompatActivity {

    private ImageView bingBackgroundView;
    private List<County>counties;
    private ArrayList<County> countyArrayList = new ArrayList<>();
    private RecyclerView cityRecyclerView;
    private RecyclerViewAdapter adapter;
    private Button addButton;
    public DrawerLayout drawerLayout;
    private int resultCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        bingBackgroundView = (ImageView)findViewById(R.id.background_image);
        cityRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_city);
        addButton = (Button)findViewById(R.id.add_button);
        drawerLayout = (DrawerLayout)findViewById(R.id.draw_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        cityRecyclerView.setLayoutManager(layoutManager);
        counties = DataSupport.where("isselected = ?","1").find(County.class);
        countyArrayList = (ArrayList<County>)counties;
        adapter = new RecyclerViewAdapter(countyArrayList);
        cityRecyclerView.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = preferences.getString("background_pic",null);
        if(bingPic != null) {
            Glide.with(this).load(bingPic).into(bingBackgroundView);
        }

        this.setResult(resultCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setRefreshing(){
        counties = DataSupport.where("isselected = ?","1").find(County.class);
        if(counties.size()>countyArrayList.size()){
            countyArrayList.add(counties.get(counties.size()-1));
            adapter.notifyItemInserted(counties.size()-1);
        }
    }

}

package com.example.remin.knowweather.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.remin.knowweather.db.City;
import com.example.remin.knowweather.db.County;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by remin.
 * Created Time 2017/9/10 ${Time}
 */

public class CityPointView extends View{
    public int currentPosition;
    private ArrayList<County> countyList = new ArrayList<>();
    private int pointsNum;//view中的圆点个数
    private Paint paint;
    private RectF rectf;
    private float[] loc = new float[4];
    private float pointLength[] = new float[4];

    /*
    * 构造函数
    * */
    public CityPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CityPointView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CityPointView(Context context) {
        this(context,null);
    }

    /*
    * 初始化画笔工具
    * */
    public void initPaint(ArrayList<County> countyList){
        this.countyList = countyList;
        paint = new Paint();

        paint.setAntiAlias(true);

//        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);
        paint.setShader(new LinearGradient(0, 100, getScreenWidth((Activity) getContext()),
                100, Color.parseColor("#ffc125"), Color.parseColor("#ff4500"), Shader.TileMode.MIRROR));
        if(countyList != null){
            pointsNum = countyList.size();
        }

        for(int i=0;i<pointsNum&&i<pointLength.length;i++){
            if(i==0){
                pointLength[i]=getScreenWidth((Activity)getContext())/10;
            }else{
                pointLength[i]=20;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(40, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i=1;i<3;i++)
        {
            loc[i]=0;
        }
        loc[0] = 20;
        for(int i=0;i<pointsNum&&i<3;i++){
            try {
                rectf = new RectF(loc[i],0,loc[i]+pointLength[i],20);
            }catch (Exception e){
                e.printStackTrace();
            }
            loc[i+1]=loc[i]+pointLength[i]+20;
            canvas.drawRoundRect(rectf,10,10,paint);
        }

        super.onDraw(canvas);
    }

    /*
* 更新导航圆点
* */
    public void updateView(float pointLength[]){
        this.pointLength = pointLength;
        invalidate();
    }

    public float getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }

}

package com.example.remin.knowweather.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.remin.knowweather.Item;
import com.example.remin.knowweather.R;
import com.example.remin.knowweather.db.County;
import com.example.remin.knowweather.gson.HourlyForecast;
import com.example.remin.knowweather.gson.Weather;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by remin.
 * Created Time 2017/8/21 ${Time}
 */

public class TestStackAdapter extends StackAdapter<Integer>{
//    private List<Item> items = new ArrayList<>();

    private int stackPosition;
    private TextView cityTitle;
    private ArrayList<Weather> weatherList ;
    private ArrayList<County> counties;
    private Context mContext;
    public TestStackAdapter(Context context,ArrayList<Weather> List,int stackPosition,TextView cityTitle,ArrayList<County>counties) {
        super(context);
        mContext = context;
        this.weatherList = List;
        this.cityTitle = cityTitle;
        this.stackPosition = stackPosition;
        this.counties = counties;
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case R.layout.single_item:
                view = getLayoutInflater().inflate(R.layout.single_item,parent,false);

//                RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
//                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//                recyclerView.setLayoutManager(layoutManager);
//                RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
//                recyclerView.setAdapter(adapter);
                return new ColorItemViewHolder(view);
//                return new ColorItemLargeHeaderViewHolder(view,mContext);
        }
        return null;
    }

    public void changeWeather(ArrayList<Weather> weatherList){
        this.weatherList = weatherList;
    }


    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
//        if(holder instanceof ColorItemLargeHeaderViewHolder){
//            ColorItemLargeHeaderViewHolder h = (ColorItemLargeHeaderViewHolder)holder;
//            h.onBind(data,position);
//        }
        //依次创建每个天气指标的holder写数据
        Weather weather = new Weather();
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            if(weatherList != null&&weatherList.get(stackPosition).basic != null) {
                weather = weatherList.get(stackPosition);
                h.onBind(data, position, weather);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return  R.layout.single_item;
            case 1:
                return  R.layout.single_item;
            case 2:
                return  R.layout.single_item;
            case 3:
            case 4:
            case 5:
                return  R.layout.single_item;
        }
        return  R.layout.single_item;
    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        TextView mTextDetail;
        LineChartView chart;

        /*
  * chart
  * */

        private AxisValue axisXValue;
        private ArrayList<AxisValue> axisXValues = new ArrayList<>();
        private AxisValue axisYValue;
        private ArrayList<AxisValue> axisYValues = new ArrayList<>();//XY轴的值
        private boolean hasAxes = true;
        private boolean hasAxesNames = true; // 横竖行的名字
        private boolean hasLines = true;
        private boolean hasPoints = true;
        private ValueShape shape = ValueShape.CIRCLE;
        private boolean isFilled = true;
        private boolean hasLabels = true; //是否显示点的数据  
        private boolean isCubic = false;
        private boolean hasLabelForSelected = false;
        private boolean pointsHaveDifferentColor;
        private LineChartData data;
        private int numberOfLines = 2; //只显示一行数据 
        private int maxNumberOfLines = 2; // 如果为4则表示最多显示4行，1表示只有一行数
        private int numberOfPoints = 8; // 每行数据有多少个点  
        // 存储数据 
        float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            mTextDetail = (TextView) view.findViewById(R.id.detail_info);
            chart = (LineChartView) mContainerContent.findViewById(R.id.chart);
        }

        @Override

        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(Integer data, int position,Weather weather) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
//            mTextTitle.setText(String.valueOf(position));
            chart.setVisibility(View.INVISIBLE);
            switch (position){
                case 0:
                    mTextDetail.setVisibility(View.GONE);
                    chart.setVisibility(View.VISIBLE);
                    mTextTitle.setText("今日气温");
                    generateDailyData(weather);
                    break;
                case 1:
                    mTextDetail.setVisibility(View.GONE);
                    chart.setVisibility(View.VISIBLE);
                    mTextTitle.setText("一周气温");
                    generateWeakData(weather);
                    break;
                case 2:
                    mTextTitle.setText("空气质量："+weather.aqi.city.airQuality);
                    break;
                case 3:
                    mTextTitle.setText("舒适度："+weather.suggestion.comfort.brf);
                    mTextDetail.setText(weather.suggestion.comfort.info);
                    break;
                case 4:
                    mTextTitle.setText("洗车指数："+weather.suggestion.carWash.brf);
                    mTextDetail.setText(weather.suggestion.carWash.info);
                    break;
                case 5:
                    mTextTitle.setText("运动指数："+weather.suggestion.sport.brf);
                    mTextDetail.setText(weather.suggestion.sport.info);
                    break;
            }
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if(state==CardStackView.ANIMATION_STATE_START&&willBeSelect){
            }
            if(state==CardStackView.ANIMATION_STATE_END&&willBeSelect){
            }
        }

        private void generateDailyData(Weather weather){
            numberOfPoints = weather.hourlyForecastList.size();
            for(int i=0;i< weather.hourlyForecastList.size();i++){
                randomNumbersTab[0][i] = Float.parseFloat(weather.hourlyForecastList.get(i).temperature);
            }
            List<Line> lines = new ArrayList<>();
            numberOfLines = 1;
            for(int i=0;i<numberOfLines;++i){
                List<PointValue> values = new ArrayList<>();
                for (int j=0;j<numberOfPoints;++j){
                    axisXValues.add(new AxisValue(j).setLabel(weather.hourlyForecastList.get(j).date.substring(10)));
                    values.add(new PointValue(j+1,randomNumbersTab[i][j]).setLabel(String.valueOf(randomNumbersTab[i][j])));
                }

                Line line = new Line(values);
                line.setColor(Color.parseColor("#FFCD41"));
                line.setShape(shape);
                line.setCubic(isCubic);
                line.setFilled(isFilled);
                line.setHasLabels(hasLabels);
//                line.setHasLabelsOnlyForSelected(hasLabelForSelected);

                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                if(pointsHaveDifferentColor){
                    line.setPointColor(R.color.color_4);
                }
                lines.add(line);
            }

            data = new LineChartData(lines);
            if (hasAxes){
                Axis axisx = new Axis();
                Axis axisy = new Axis().setHasLines(true);
                if (hasAxesNames){
                    axisx.setName("");
                    axisy.setName("");
                    axisx.setLineColor(Color.parseColor("#FFCD41"));
                    axisx.setTextColor(Color.parseColor("#FFCD41"));
                    axisy.setTextColor(Color.parseColor("#FFCD41"));
                    axisy.setLineColor(Color.parseColor("#FFCD41"));
                    axisx.setValues(axisXValues);
                    axisy.setValues(axisYValues);
                }
                data.setAxisXBottom(axisx);
                data.setAxisYLeft(axisy);
            }else{
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }
            data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);
        }

        private void generateWeakData(Weather weather){
            numberOfPoints = weather.forecastList.size();
            for(int i=0;i< numberOfPoints;i++){
                randomNumbersTab[0][i] = Float.parseFloat(weather.forecastList.get(i).temperature.max);
                randomNumbersTab[1][i] = Float.parseFloat(weather.forecastList.get(i).temperature.min);
            }

            List<Line> lines = new ArrayList<>();
            numberOfLines = 2;
            for(int i=0;i<numberOfLines;++i){
                List<PointValue> values = new ArrayList<>();
                for (int j=0;j<numberOfPoints;++j){
                    axisXValues.add(new AxisValue(j).setLabel(weather.forecastList.get(j).date.substring(5)));
                    values.add(new PointValue(j+1,randomNumbersTab[i][j]).setLabel(String.valueOf(randomNumbersTab[i][j])));
                }

                Line line = new Line(values);
                if(i == 0){
                    line.setColor(Color.parseColor("#D81B60"));
                    line.setFilled(false);
                }else{
                    line.setColor(Color.parseColor("#FFCD41"));
                    line.setFilled(false);
                    line.setSquare(true);

                }
                line.setShape(shape);
                line.setCubic(isCubic);
                line.setHasLabels(hasLabels);
//                line.setHasLabelsOnlyForSelected(hasLabelForSelected);

                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                if(pointsHaveDifferentColor){
                    line.setPointColor(R.color.color_4);
                }
                lines.add(line);
            }

            data = new LineChartData(lines);
            if (hasAxes){
                Axis axisx = new Axis();
                Axis axisy = new Axis().setHasLines(true);
                if (hasAxesNames){
                    axisx.setName("");
                    axisy.setName("");
                    axisx.setLineColor(Color.parseColor("#FFCD41"));
                    axisx.setTextColor(Color.parseColor("#FFCD41"));
                    axisy.setTextColor(Color.parseColor("#FFCD41"));
                    axisy.setLineColor(Color.parseColor("#FFCD41"));
                    axisx.setValues(axisXValues);
                }
                data.setAxisXBottom(axisx);
                data.setAxisYLeft(axisy);
            }else{
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }
            data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);

        }


    }
}

package com.itskush.kushal.sunshine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kush on 17-07-2016.
 */
public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.MyViewHolder> {

    private List<recyclerViewModel> stringList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView headingText, subHeadingText, baseText, low, humidity;
        public ImageView forecastImageView;

        public MyViewHolder(View view) {
            super(view);
            headingText = (TextView) view.findViewById(R.id.heading_tv_recycler_view);
            forecastImageView = (ImageView) view.findViewById(R.id.forecastImageView);
            subHeadingText = (TextView) view.findViewById(R.id.sub_heading_tv_recycler_view);
            baseText = (TextView) view.findViewById(R.id.base_heading_tv_recycler_view);
            low = (TextView) view.findViewById(R.id.base_low_tv_recycler_view);
            humidity = (TextView) view.findViewById(R.id.humidity_tv_recycler_view);
        }

    }

    public recyclerViewAdapter(List<recyclerViewModel> stringList) {
        this.stringList = stringList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        recyclerViewModel recyclerViewModel = stringList.get(position);
        holder.headingText.setText(recyclerViewModel.getHeadingText());
        holder.forecastImageView.setImageResource(recyclerViewModel.getForecastImageView());
        holder.subHeadingText.setText(recyclerViewModel.getSubHeadingText());
        holder.baseText.setText(recyclerViewModel.getBaseText());
        holder.low.setText(recyclerViewModel.getLow());
        holder.humidity.setText(recyclerViewModel.getHumidity());
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

}

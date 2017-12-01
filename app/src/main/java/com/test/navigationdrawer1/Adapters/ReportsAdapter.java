package com.test.navigationdrawer1.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.navigationdrawer1.R;
import com.test.navigationdrawer1.REST.Models.ReporteIncidente;

import java.util.List;

/**
 * Created by H250490 on 01-Dec-17.
 */

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {
    private List<ReporteIncidente> reportsList;
    private Fragment parent;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView report_item_datetime, report_item_desc;
        public ImageView report_item_image;

        public MyViewHolder(View view) {
            super(view);
            report_item_image = (ImageView) view.findViewById(R.id.report_item_image);
            report_item_datetime = (TextView) view.findViewById(R.id.report_item_datetime);
            report_item_desc = (TextView) view.findViewById(R.id.report_item_desc);
        }
    }


    public ReportsAdapter(List<ReporteIncidente> reportsList) {
        this.reportsList = reportsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReporteIncidente reporteIncidente = reportsList.get(position);
        holder.report_item_datetime.setText(reporteIncidente.fecha + " " + reporteIncidente.hora);
        holder.report_item_desc.setText(reporteIncidente.descripcion);

        String url = reporteIncidente.imagenReporte.equals("") ?
                "http://floating-sands-67659.herokuapp.com/uploads/image.jpg" :
                reporteIncidente.imagenReporte;

        // https://stackoverflow.com/a/43971942
        Glide.with(parent)
                .load(url)
                .placeholder(R.drawable.ic_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.report_item_image);
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }
}

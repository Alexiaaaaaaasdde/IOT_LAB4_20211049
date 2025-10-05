package com.example.iot_lab4_20211049.forecast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iot_lab4_20211049.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.VH> {

    private final List<ForecastResponse.ForecastDay> data = new ArrayList<>();
    private String locationLabel = "";
    private int idLocation = -1;

    public void setHeader(String label, int id) {
        this.locationLabel = label;
        this.idLocation = id;
    }

    public void submit(List<ForecastResponse.ForecastDay> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int vt) {
        View v = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_forecast_day, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int i) {
        ForecastResponse.ForecastDay d = data.get(i);

        h.tvDate.setText("Fecha: " + d.date);

        String condTxt = (d.day != null && d.day.condition != null) ? d.day.condition.text : "";
        h.tvCond.setText("Condición: " + condTxt);

        double max = (d.day != null) ? d.day.maxtempC : 0;
        double min = (d.day != null) ? d.day.mintempC : 0;
        h.tvRange.setText(String.format(Locale.US, "Máx: %.1f°C   |   Mín: %.1f°C", max, min));

        String icon = (d.day != null && d.day.condition != null) ? d.day.condition.icon : null;
        if (icon != null && icon.startsWith("//")) icon = "https:" + icon;
        Glide.with(h.ivIcon.getContext()).load(icon).into(h.ivIcon);

        h.tvFooter.setText("Location: " + locationLabel + "   |   ID: " + idLocation);
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvDate, tvCond, tvRange, tvFooter;
        VH(@NonNull View v) {
            super(v);
            ivIcon  = v.findViewById(R.id.ivIcon);
            tvDate  = v.findViewById(R.id.tvDate);
            tvCond  = v.findViewById(R.id.tvCond);
            tvRange = v.findViewById(R.id.tvRange);
            tvFooter= v.findViewById(R.id.tvFooter);
        }
    }
}

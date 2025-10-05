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

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.VH> {

    private final List<ForecastResponse.Hour> data = new ArrayList<>();
    private String locLabel = "";
    private String idLocation = "";

    public void setHeader(String locLabel, String idLocation) {
        this.locLabel = locLabel;
        this.idLocation = idLocation;
        notifyDataSetChanged();
    }

    public void submit(List<ForecastResponse.Hour> hours) {
        data.clear();
        if (hours != null) data.addAll(hours);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hour, parent, false);
        return new VH(v);
    }

    //Se hace uso de LLM
    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ForecastResponse.Hour hr = data.get(pos);

        h.tvTime.setText(hr.time);
        h.tvCond.setText(hr.condition != null ? hr.condition.text : "");

        String metrics = String.format(Locale.US,
                "Temp: %.1f°C   Humedad: %d%%",
                hr.temp_c, hr.humidity);
        h.tvMetrics.setText(metrics);

        h.tvExtra.setText("Lluvia: " + hr.chance_of_rain + "%   Viento: " + Math.round(hr.wind_kph) + " km/h");
        h.tvFooter.setText(locLabel + "  |  ID: " + idLocation);

        if (hr.condition != null && hr.condition.icon != null) {
            String url = hr.condition.icon.startsWith("//") ? "https:" + hr.condition.icon : hr.condition.icon;
            Glide.with(h.ivIcon.getContext()).load(url).into(h.ivIcon);
        } else {
            h.ivIcon.setImageDrawable(null);
        }
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTime, tvCond, tvMetrics, tvExtra, tvFooter;
        VH(@NonNull View v) {
            super(v);
            ivIcon   = v.findViewById(R.id.ivIconH);
            tvTime   = v.findViewById(R.id.tvTime);
            tvCond   = v.findViewById(R.id.tvCondH);
            tvMetrics= v.findViewById(R.id.tvMetrics);
            tvExtra  = v.findViewById(R.id.tvExtra);
            tvFooter = v.findViewById(R.id.tvFooterH);
        }
    }
}

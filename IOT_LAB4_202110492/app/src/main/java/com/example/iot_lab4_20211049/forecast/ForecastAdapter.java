package com.example.iot_lab4_20211049.forecast;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.iot_lab4_20211049.R;
import java.util.*;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.VH> {
    private List<ForecastResponse.ForecastDay> data = new ArrayList<>();
    private String locationLabel = ""; private int idLocation = -1;

    public void setHeader(String loc,int id){ locationLabel = loc; idLocation = id; }
    public void submit(List<ForecastResponse.ForecastDay> items){ data = items; notifyDataSetChanged(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivIcon; TextView tvDate,tvCond,tvTemps,tvLoc;
        VH(View v){ super(v);
            ivIcon=v.findViewById(R.id.ivIcon);
            tvDate=v.findViewById(R.id.tvDate);
            tvCond=v.findViewById(R.id.tvCond);
            tvTemps=v.findViewById(R.id.tvTemps);
            tvLoc=v.findViewById(R.id.tvLoc);
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p,int vt){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_forecast_day, p, false));
    }
    @Override public void onBindViewHolder(@NonNull VH h,int i){
        ForecastResponse.ForecastDay d = data.get(i);
        h.tvDate.setText("Fecha: " + d.date);
        if (d.day!=null){
            h.tvCond.setText("Condición: " + (d.day.condition!=null? d.day.condition.text : "-"));
            h.tvTemps.setText(String.format(Locale.US,"Máx: %.1f°C  |  Mín: %.1f°C", d.day.maxtemp_c, d.day.mintemp_c));
            if (d.day.condition!=null && d.day.condition.icon!=null)
                Glide.with(h.itemView.getContext()).load("https:" + d.day.condition.icon).into(h.ivIcon);
        }
        h.tvLoc.setText("Location: " + locationLabel + "  |  ID: " + idLocation);
    }
    @Override public int getItemCount(){ return data.size(); }
}

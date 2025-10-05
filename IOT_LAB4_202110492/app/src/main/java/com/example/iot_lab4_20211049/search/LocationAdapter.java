package com.example.iot_lab4_20211049.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iot_lab4_20211049.R;
import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.VH> {
    public interface OnClick { void onItem(LocationItem item); }
    private List<LocationItem> data = new ArrayList<>();
    private final OnClick onClick;

    public LocationAdapter(OnClick onClick){ this.onClick = onClick; }
    public void submit(List<LocationItem> items){ data = items; notifyDataSetChanged(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSub;
        VH(View v){ super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvSub = v.findViewById(R.id.tvSub);
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vt){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_location, p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int i){
        LocationItem it = data.get(i);
        h.tvTitle.setText(it.name + " (" + it.id + ")");
        h.tvSub.setText(it.region + ", " + it.country + "  ["+it.lat+", "+it.lon+"]");
        h.itemView.setOnClickListener(v -> onClick.onItem(it));
    }

    @Override public int getItemCount(){ return data.size(); }
}

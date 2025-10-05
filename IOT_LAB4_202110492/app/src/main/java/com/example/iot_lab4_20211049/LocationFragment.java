package com.example.iot_lab4_20211049;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iot_lab4_20211049.net.ApiClient;
import com.example.iot_lab4_20211049.net.WeatherApi;
import com.example.iot_lab4_20211049.search.LocationAdapter;
import com.example.iot_lab4_20211049.search.LocationItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragment extends Fragment {
    private static final String API_KEY = "5586a0acd5a345e0b4361158250210";
    private EditText etQuery;
    private LocationAdapter adapter;
    private WeatherApi api;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b){
        View v = inf.inflate(R.layout.fragment_location, c, false);
        etQuery = v.findViewById(R.id.etQuery);
        RecyclerView rv = v.findViewById(R.id.rvLocations);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LocationAdapter(item -> {
            Bundle args = new Bundle();
            args.putInt("idLocation", item.id);
            ForecasterFragment f = new ForecasterFragment();
            f.setArguments(args);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_container, f)
                    .commit();
        });
        rv.setAdapter(adapter);
        api = ApiClient.get().create(WeatherApi.class);
        v.findViewById(R.id.btnBuscar).setOnClickListener(x -> doSearch());
        return v;
    }

    private void doSearch(){
        String q = etQuery.getText().toString().trim();
        if (q.isEmpty()) { etQuery.setError("Ingresa una locación (ej: miraflores lima)"); return; }
        api.search(API_KEY, q).enqueue(new Callback<List<LocationItem>>() {
            @Override public void onResponse(Call<List<LocationItem>> c, Response<List<LocationItem>> r) {
                if (!isAdded()) return;
                if (r.isSuccessful() && r.body()!=null) adapter.submit(r.body());
                else Toast.makeText(getContext(),"Sin resultados",Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Call<List<LocationItem>> c, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(),"Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

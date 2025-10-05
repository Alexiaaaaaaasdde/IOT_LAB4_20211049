package com.example.iot_lab4_20211049;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab4_20211049.forecast.ForecastAdapter;
import com.example.iot_lab4_20211049.forecast.ForecastResponse;
import com.example.iot_lab4_20211049.net.ApiClient;
import com.example.iot_lab4_20211049.net.WeatherApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecasterFragment extends Fragment {
    private static final String API_KEY = "5586a0acd5a345e0b4361158250210";

    private ForecastAdapter adapter;
    private WeatherApi api;
    private EditText etIdLoc, etDays;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf,
                             @Nullable ViewGroup c,
                             @Nullable Bundle b) {
        View v = inf.inflate(R.layout.fragment_forecaster, c, false);

        RecyclerView rv = v.findViewById(R.id.rvForecast);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ForecastAdapter();
        rv.setAdapter(adapter);

        etIdLoc = v.findViewById(R.id.etIdLoc);
        etDays  = v.findViewById(R.id.etDays);
        v.findViewById(R.id.btnBuscarFore).setOnClickListener(x -> triggerSearch());

        // Enter en #days también dispara la búsqueda
        etDays.setOnEditorActionListener((tv, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                triggerSearch();
                return true;
            }
            return false;
        });

        api = ApiClient.get().create(WeatherApi.class);

        // Si viene desde LocationFragment con el argumento idLocation, autoprefill y buscar 14 días
        int idFromArgs = getArguments() != null ? getArguments().getInt("idLocation", -1) : -1;
        if (idFromArgs != -1) {
            etIdLoc.setText(String.valueOf(idFromArgs));
            etDays.setText("14");
            fetchForecast(idFromArgs, 14);
        } else {
            Toast.makeText(getContext(), "Ingresa idLocation y #días (1-14)", Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    private void triggerSearch() {
        String sId   = etIdLoc.getText().toString().trim();
        String sDays = etDays.getText().toString().trim();

        if (sId.isEmpty())   { etIdLoc.setError("Requerido"); return; }
        if (sDays.isEmpty()) { etDays.setError("Requerido"); return; }

        int id, days;
        try { id = Integer.parseInt(sId); }   catch (Exception e) { etIdLoc.setError("Numérico"); return; }
        try { days = Integer.parseInt(sDays);}catch (Exception e) { etDays.setError("Numérico"); return; }

        if (id <= 0)                 { etIdLoc.setError("> 0"); return; }
        if (days < 1 || days > 14)   { etDays.setError("1 a 14"); return; }

        fetchForecast(id, days);
    }

    private void fetchForecast(int idLoc, int days) {
        // Limpia la lista mientras carga
        adapter.submit(java.util.Collections.emptyList());

        api.forecast(API_KEY, "id:" + idLoc, days).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> c, Response<ForecastResponse> r) {
                if (!isAdded()) return;
                if (r.isSuccessful() && r.body() != null && r.body().forecast != null) {
                    String locLabel = r.body().location != null
                            ? (r.body().location.name + ", " + r.body().location.region)
                            : "(desconocido)";
                    adapter.setHeader(locLabel, idLoc);
                    adapter.submit(r.body().forecast.forecastday);

                    if (r.body().forecast.forecastday == null ||
                            r.body().forecast.forecastday.isEmpty()) {
                        Toast.makeText(getContext(), "Sin resultados", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Sin pronóstico", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> c, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

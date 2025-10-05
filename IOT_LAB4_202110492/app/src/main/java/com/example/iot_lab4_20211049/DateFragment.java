package com.example.iot_lab4_20211049;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab4_20211049.forecast.ForecastResponse;
import com.example.iot_lab4_20211049.forecast.HourAdapter;
import com.example.iot_lab4_20211049.net.ApiClient;
import com.example.iot_lab4_20211049.net.WeatherApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateFragment extends Fragment {
    //Se hace uso de LLM para entender como usar las API_KEY de OpenWeatherMap
    private static final String API_KEY = "5586a0acd5a345e0b4361158250210";

    private WeatherApi api;
    private HourAdapter adapter;
    private EditText etId, etDate;
    private TextView tvMeta, tvTitle;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle b) {
        View v = inf.inflate(R.layout.fragment_date, c, false);

        tvTitle = v.findViewById(R.id.tvTitleDate);
        tvMeta  = v.findViewById(R.id.tvMeta);
        etId    = v.findViewById(R.id.etIdLoc2);
        etDate  = v.findViewById(R.id.etDate);
        v.findViewById(R.id.btnBuscarDate).setOnClickListener(x -> triggerSearch());

        RecyclerView rv = v.findViewById(R.id.rvHours);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HourAdapter();
        rv.setAdapter(adapter);

        api = ApiClient.get().create(WeatherApi.class);

        // si vienes desde LocationFragment con idLocation
        int idFromArgs = getArguments()!=null ? getArguments().getInt("idLocation", -1) : -1;
        if (idFromArgs != -1) etId.setText(String.valueOf(idFromArgs));

        etDate.setOnEditorActionListener((tv, aid, e) -> {
            if (aid == EditorInfo.IME_ACTION_SEARCH || aid == EditorInfo.IME_ACTION_DONE) {
                triggerSearch();
                return true;
            }
            return false;
        });

        // título por defecto
        tvTitle.setText("Pronóstico Futuro");
        tvMeta.setText("Ingresa idLocation y fecha (YYYY-MM-DD), luego Buscar.");

        return v;
    }

    private void triggerSearch() {
        String sId = etId.getText().toString().trim();
        String sDt = etDate.getText().toString().trim();

        if (TextUtils.isEmpty(sId)) { etId.setError("Requerido"); return; }
        if (TextUtils.isEmpty(sDt)) { etDate.setError("AAAA-MM-DD"); return; }

        int id;
        try { id = Integer.parseInt(sId); }
        catch (Exception e) { etId.setError("Numérico"); return; }

        if (Build.VERSION.SDK_INT < 26) {
            Toast.makeText(getContext(), "Requiere API 26+ para comparar fechas", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate today, date;
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
            date = LocalDate.parse(sDt, fmt);
            today = LocalDate.now();
        } catch (Exception e) {
            etDate.setError("Formato YYYY-MM-DD");
            return;
        }

        long diff = ChronoUnit.DAYS.between(today, date); // futuro:+ pasado:-
        String q = "id:" + id;

        adapter.submit(java.util.Collections.emptyList());
        tvMeta.setText("Buscando…");

        if (diff < 0 && diff >= -365) {
            callHistory(q, sDt, "history");
        } else if (diff >= 0 && diff <= 14) {
            callForecastByDate(q, sDt, "forecast");
        } else if (diff >= 15 && diff <= 300) {
            callFuture(q, sDt, "future");
        } else {
            Toast.makeText(getContext(), "Rango permitido: -365 a +300 días", Toast.LENGTH_LONG).show();
        }
    }

    private void callForecastByDate(String q, String dateIso, String via) {
        api.forecastByDate(API_KEY, q, dateIso).enqueue(new Callback<ForecastResponse>() {
            @Override public void onResponse(Call<ForecastResponse> c, Response<ForecastResponse> r) {
                handleResponse(r, q, dateIso, via);
            }
            @Override public void onFailure(Call<ForecastResponse> c, Throwable t) {
                if (!isAdded()) return;
                tvMeta.setText("Error: " + t.getMessage());
                Toast.makeText(getContext(),"Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callHistory(String q, String dateIso, String via) {
        api.historyByDate(API_KEY, q, dateIso).enqueue(new Callback<ForecastResponse>() {
            @Override public void onResponse(Call<ForecastResponse> c, Response<ForecastResponse> r) {
                handleResponse(r, q, dateIso, via);
            }
            @Override public void onFailure(Call<ForecastResponse> c, Throwable t) {
                if (!isAdded()) return;
                tvMeta.setText("Error: " + t.getMessage());
                Toast.makeText(getContext(),"Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callFuture(String q, String dateIso, String via) {
        api.futureByDate(API_KEY, q, dateIso).enqueue(new Callback<ForecastResponse>() {
            @Override public void onResponse(Call<ForecastResponse> c, Response<ForecastResponse> r) {
                handleResponse(r, q, dateIso, via);
            }
            @Override public void onFailure(Call<ForecastResponse> c, Throwable t) {
                if (!isAdded()) return;
                tvMeta.setText("Error: " + t.getMessage());
                Toast.makeText(getContext(),"Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResponse(Response<ForecastResponse> r, String q, String dateIso, String via) {
        if (!isAdded()) return;

        if (r.isSuccessful() && r.body()!=null && r.body().forecast!=null
                && r.body().forecast.forecastday!=null && !r.body().forecast.forecastday.isEmpty()) {

            ForecastResponse.Location loc = r.body().location;
            ForecastResponse.ForecastDay day = r.body().forecast.forecastday.get(0);
            List<ForecastResponse.Hour> hours = day.hour;

            String locLabel = (loc!=null ? (loc.name + ", " + loc.region) : "(desconocido)");
            String idOnly   = q.startsWith("id:") ? q.substring(3) : q;

            tvMeta.setText("Lugar: " + locLabel + "   |   id: " + idOnly
                    + "   |   fecha: " + day.date + "   |   vía: " + via);

            adapter.setHeader(locLabel, idOnly);  // <<< importante
            adapter.submit(hours);

            if ("history".equals(via))      tvTitle.setText("Pronóstico Histórico");
            else if ("future".equals(via))  tvTitle.setText("Pronóstico Futuro");
            else                            tvTitle.setText("Pronóstico por Fecha");

        } else {
            tvMeta.setText("Sin resultados");
            Toast.makeText(getContext(),"Sin resultados",Toast.LENGTH_SHORT).show();
        }
    }
}

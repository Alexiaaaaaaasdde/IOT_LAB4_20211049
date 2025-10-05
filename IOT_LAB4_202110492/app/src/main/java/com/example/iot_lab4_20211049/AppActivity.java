package com.example.iot_lab4_20211049;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AppActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        replaceRoot(new LocationFragment());

        findViewById(R.id.btnFuture).setOnClickListener(v -> replaceRoot(new DateFragment()));
        findViewById(R.id.btnLocation).setOnClickListener(v -> replaceRoot(new LocationFragment()));
        findViewById(R.id.btnForecaster).setOnClickListener(v -> replaceRoot(new ForecasterFragment()));
        findViewById(R.id.btnFuture).setOnClickListener(v -> replaceRoot(new LocationFragment())); // placeholder
    }

    private void replaceRoot(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_container, f)
                .commit();
    }
}

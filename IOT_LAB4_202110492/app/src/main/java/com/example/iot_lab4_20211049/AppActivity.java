package com.example.iot_lab4_20211049;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AppActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_app);


        if (b == null) {
            replaceRoot(new LocationFragment());
        }

        findViewById(R.id.btnLocation).setOnClickListener(v ->
                replaceRoot(new LocationFragment())
        );

        findViewById(R.id.btnForecaster).setOnClickListener(v ->
                replaceRoot(new ForecasterFragment())
        );

        findViewById(R.id.btnFuture).setOnClickListener(v ->
                replaceRoot(new DateFragment())
        );
    }

    private void replaceRoot(androidx.fragment.app.Fragment f){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_container, f)
                .commit();
    }
}

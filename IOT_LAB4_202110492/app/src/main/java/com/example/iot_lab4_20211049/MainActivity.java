package com.example.iot_lab4_20211049;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ⚠️ activity_main.xml debe tener android:id="@+id/main" en la vista raíz
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón "Ingresar" -> valida Internet -> entra a AppActivity
        Button btn = findViewById(R.id.btnIngresar);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                if (!isConnected()) {
                    new AlertDialog.Builder(this)
                            .setTitle("Sin conexión")
                            .setMessage("No hay Internet. Ve a Configuración para habilitarla.")
                            .setPositiveButton("Configuración",
                                    (d, w) -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                            .setNegativeButton("Cancelar", null)
                            .show();
                } else {
                    startActivity(new Intent(this, AppActivity.class));
                }
            });
        }
    }

    // ✅ Chequeo de conectividad (WiFi/Datos/Ethernet)
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return caps != null && (
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        );
    }
}

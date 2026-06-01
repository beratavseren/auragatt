package org.example.auramesh;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 1. İzinlerin önceden verilip verilmediğini kontrol et
        if (hasAllRequiredPermissions()) {

            // PermissionActivity'yi atladığımız için Mesh (Ağ) servislerini burada başlatmamız gerekiyor
            ((AuraMeshApplication) getApplication()).startMeshServices();

            // 2. Kullanıcının daha önce profilini doldurup doldurmadığını kontrol et
            SharedPreferences prefs = getSharedPreferences("AuraMeshPrefs", MODE_PRIVATE);
            String savedName = prefs.getString("profile_name", "");

            if (!savedName.isEmpty()) {
                // Hem izinler verilmiş hem profil doldurulmuşsa doğrudan Ana Ekrana git
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            } else {
                // İzinler verilmiş ama profil eksikse Profil Ekrana git
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }

            // MainActivity'yi kapatıyoruz ki geri tuşuna basınca başlangıç ekranına dönmesin
            finish();
            return;
        }

        // Eğer izinler henüz verilmemişse standart başlangıç ekranını ve BAŞLA butonunu göster
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStart).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PermissionActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Gerekli tüm izinlerin verilip verilmediğini denetleyen yardımcı metod
    private boolean hasAllRequiredPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
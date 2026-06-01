package org.example.auramesh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Yeni eklenen importlar
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class HomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable sosRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- TAM EKRAN (IMMERSIVE MODE) KODLARI BAŞLANGICI ---
        // Pencerenin sistem çubuklarının altına kadar uzanmasını sağlar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        if (windowInsetsController != null) {
            // Sadece alt navigasyon çubuğunu gizlemek istersen: WindowInsetsCompat.Type.navigationBars()
            // Hem üst durum çubuğunu hem alt çubuğu gizlemek istersen: WindowInsetsCompat.Type.systemBars()
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());

            // Kullanıcı ekranın altından yukarı kaydırdığında çubuğun geçici olarak görünmesini sağlar
            windowInsetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }
        // --- TAM EKRAN (IMMERSIVE MODE) KODLARI BİTİŞİ ---

        setContentView(R.layout.activity_home);

        ImageView btnSos = findViewById(R.id.btnSos);
        btnSos.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                sosRunnable = () -> startActivity(new Intent(HomeActivity.this, AfetActivity.class));
                handler.postDelayed(sosRunnable, 3000);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                handler.removeCallbacks(sosRunnable);
            }
            return true;
        });

        findViewById(R.id.topProfileArea).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));

        findViewById(R.id.topBatteryArea).setOnClickListener(v ->
                Toast.makeText(this, "Ağ Durumu: Aktif", Toast.LENGTH_SHORT).show());

        findViewById(R.id.navMessage).setOnClickListener(v ->
                startActivity(new Intent(this, MessageActivity.class)));

        findViewById(R.id.navProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }
}

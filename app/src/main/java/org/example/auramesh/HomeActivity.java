package org.example.auramesh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable sosRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

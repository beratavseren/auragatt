package org.example.auramesh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Tam ekran (Immersive Mode) importları
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import org.example.auramesh.data.models.AuraMessage;
import org.example.auramesh.events.UiToRouterEvents.UserSendMessageEvent;
import org.example.auramesh.utils.AppConstants;
import org.example.auramesh.utils.AuraIdentityManager;
import org.greenrobot.eventbus.EventBus;
import java.util.UUID;

public class AfetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- TAM EKRAN (IMMERSIVE MODE) KODLARI ---
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        if (windowInsetsController != null) {
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
            windowInsetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }
        // ------------------------------------------

        setContentView(R.layout.activity_afet);

        findViewById(R.id.btnEnkaz).setOnClickListener(v -> {
            sendEmergencyMessage("ENKAZ ALTINDAYIM, YARDIM BEKLİYORUM!");
            Toast.makeText(this, "Enkaz bildirimi gönderildi.", Toast.LENGTH_SHORT).show();
            goToMessages();
        });

        findViewById(R.id.btnYarali).setOnClickListener(v -> {
            sendEmergencyMessage("YARALIYIM, ACİL MÜDAHALE GEREKLİ!");
            Toast.makeText(this, "Yaralı bildirimi gönderildi.", Toast.LENGTH_SHORT).show();
            goToMessages();
        });

        findViewById(R.id.btnGuvendeyim).setOnClickListener(v -> {
            sendEmergencyMessage("GÜVENDEYİM, DURUMUM İYİ.");
            Toast.makeText(this, "Güvende olduğunuz bildirildi.", Toast.LENGTH_SHORT).show();
            goToMessages();
        });

        findViewById(R.id.navHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        findViewById(R.id.navMessage).setOnClickListener(v -> goToMessages());

        findViewById(R.id.navProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void sendEmergencyMessage(String text) {
        String myNodeId = AuraIdentityManager.getMyNodeId(this);
        AuraMessage msg = new AuraMessage(
                UUID.randomUUID().toString(),
                myNodeId,
                AppConstants.TARGET_PUBLIC,
                text,
                System.currentTimeMillis(),
                AppConstants.DEFAULT_MESSAGE_TTL * 7 // Acil mesajlar 7 gün TTL
        );
        EventBus.getDefault().post(new UserSendMessageEvent(msg));
    }

    private void goToMessages() {
        startActivity(new Intent(this, MessageActivity.class));
    }
}

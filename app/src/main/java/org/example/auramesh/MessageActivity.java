package org.example.auramesh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.example.auramesh.data.local.AuraDatabase;
import org.example.auramesh.data.models.AuraMessage;
import org.example.auramesh.events.RouterToUiEvent.UpdateUiEvent;
import org.example.auramesh.events.UiToRouterEvents.UserSendMessageEvent;
import org.example.auramesh.utils.AppConstants;
import org.example.auramesh.utils.AuraIdentityManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private MessageAdapter adapter;
    private EditText edtMessageInput;
    private ImageView btnSendMessage;
    private List<AuraMessage> messageList = new ArrayList<>();

    // Hızlı Mesaj Değişkenleri
    private LinearLayout layoutQuickActions;
    private ImageView btnLightning;

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

        setContentView(R.layout.activity_message);

        // Arayüz Elemanlarını Bağlama
        rvMessages = findViewById(R.id.rvMessages);
        edtMessageInput = findViewById(R.id.edtMessageInput);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnLightning = findViewById(R.id.btnLightning);
        layoutQuickActions = findViewById(R.id.layoutQuickActions);

        TextView btnQuickEnkaz = findViewById(R.id.btnQuickEnkaz);
        TextView btnQuickYarali = findViewById(R.id.btnQuickYarali);
        TextView btnQuickGuven = findViewById(R.id.btnQuickGuven);

        // RecyclerView Ayarları
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(this, messageList);
        rvMessages.setAdapter(adapter);

        rvMessages.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) scrollToBottom();
        });

        loadMessagesFromDb();

        // Normal Mesaj Gönderme Butonu
        btnSendMessage.setOnClickListener(v -> {
            String text = edtMessageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                edtMessageInput.setText("");
            }
        });

        // --- HIZLI MESAJ İŞLEMLERİ BAŞLANGICI ---
        btnLightning.setOnClickListener(v -> {
            if (layoutQuickActions.getVisibility() == View.VISIBLE) {
                layoutQuickActions.setVisibility(View.GONE);
            } else {
                layoutQuickActions.setVisibility(View.VISIBLE);
            }
        });

        btnQuickEnkaz.setOnClickListener(v -> {
            sendMessage("Enkaz Altındayım");
            layoutQuickActions.setVisibility(View.GONE);
        });

        btnQuickYarali.setOnClickListener(v -> {
            sendMessage("Yaralıyım");
            layoutQuickActions.setVisibility(View.GONE);
        });

        btnQuickGuven.setOnClickListener(v -> {
            sendMessage("Güvendeyim");
            layoutQuickActions.setVisibility(View.GONE);
        });
        // --- HIZLI MESAJ İŞLEMLERİ BİTİŞİ ---

        // Alt Navigasyon Yönlendirmeleri
        findViewById(R.id.navHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }

    private void loadMessagesFromDb() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<AuraMessage> dbMessages = AuraDatabase.getDatabase(this).auraMessageDao().getAllMessages();
            runOnUiThread(() -> {
                messageList.clear();
                messageList.addAll(dbMessages);
                adapter.notifyDataSetChanged();
                scrollToBottom();
            });
        });
    }

    private void sendMessage(String text) {
        String myNodeId = AuraIdentityManager.getMyNodeId(this);
        String messageId = UUID.randomUUID().toString();
        AuraMessage msg = new AuraMessage(
                messageId,
                myNodeId,
                AppConstants.TARGET_PUBLIC,
                text,
                System.currentTimeMillis(),
                AppConstants.DEFAULT_MESSAGE_TTL
        );
        EventBus.getDefault().post(new UserSendMessageEvent(msg));

        // Optimistic UI update
        runOnUiThread(() -> {
            messageList.add(msg);
            adapter.notifyItemInserted(messageList.size() - 1);
            scrollToBottom();
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateUi(UpdateUiEvent event) {
        loadMessagesFromDb();
    }

    private void scrollToBottom() {
        if (adapter != null && adapter.getItemCount() > 0) {
            rvMessages.post(() -> rvMessages.scrollToPosition(adapter.getItemCount() - 1));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
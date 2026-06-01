package org.example.auramesh.routing;

import android.content.Context;
import android.util.Log;

import org.example.auramesh.Register.BleAdvertiseRegister;
import org.example.auramesh.data.local.AuraDatabase;
import org.example.auramesh.data.local.AuraMessageDao;
import org.example.auramesh.data.models.AuraMessage;
import org.example.auramesh.events.HardwareToRegisterEvents.NewMessagesSavedToDatabaseEvent;
import org.example.auramesh.events.HardwareToRegisterEvents.TriggerTtlCleanupEvent;
import org.example.auramesh.events.RouterToUiEvent.UpdateUiEvent;
import org.example.auramesh.utils.AuraIdentityManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StateManager {

    private static final String TAG = "StateManager";
    private final AuraMessageDao messageDao;
    private final AtomicLong currentGlobalHash = new AtomicLong(0L);
    private final AtomicInteger currentMessageCount = new AtomicInteger(0);
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    public StateManager(Context context) {
        this.messageDao = AuraDatabase.getDatabase(context).auraMessageDao();

        BleAdvertiseRegister.getInstance().updateNodeId(AuraIdentityManager.getMyNodeId(context));
        EventBus.getDefault().register(this);
        initializeStateFromDatabase();
    }

    private void initializeStateFromDatabase() {
        dbExecutor.execute(() -> {
            List<AuraMessage> allMessages = messageDao.getAllMessages();
            currentMessageCount.set(allMessages.size());

            long tempHash = 0L;
            for (AuraMessage msg : allMessages) {
                tempHash ^= generate64BitHash(msg.messageId);
            }
            currentGlobalHash.set(tempHash);

            updateBleRegister();
            Log.d(TAG, "Başlangıç Durumu Yüklendi. Sayı: " + currentMessageCount.get() + " Hash: " + getHexHash());
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onNewMessagesSavedEvent(NewMessagesSavedToDatabaseEvent event) {
        dbExecutor.execute(() -> {
            for (AuraMessage msg : event.auraMessages) {
                long msgHash = generate64BitHash(msg.messageId);

                currentGlobalHash.updateAndGet(current -> current ^ msgHash);
                currentMessageCount.incrementAndGet();
            }

            EventBus.getDefault().post(new UpdateUiEvent());
            updateBleRegister();
        });
    }

    //todo: herhangi bir nedenden dolayı veri tabanından silinmez ama statemanagerda hashten düşebilir mi diye bak önlem al
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onTriggerTtlCleanupEvent(TriggerTtlCleanupEvent event) {
        dbExecutor.execute(() -> {
            List<AuraMessage> expiredMessages = messageDao.getExpiredMessages(System.currentTimeMillis());

            if (expiredMessages.isEmpty()) return;

            List<String> expiredMessageIds = new ArrayList<>();
            for (AuraMessage msg : expiredMessages) {
                expiredMessageIds.add(msg.messageId);
            }

            messageDao.deleteMessagesByIds(expiredMessageIds);

            removeMessagesFromState(expiredMessageIds);
        });
    }

    private void removeMessagesFromState(List<String> messageIds) {
        for (String msgId : messageIds) {
            long msgHash = generate64BitHash(msgId);

            currentGlobalHash.updateAndGet(current -> current ^ msgHash);
            currentMessageCount.decrementAndGet();
        }
        updateBleRegister();
    }

    // Dışarıdan çağrılmak istendiğinde sıraya girmesi için
    public void deleteMessages(List<String> messageIds) {
        dbExecutor.execute(() -> {
            removeMessagesFromState(messageIds);
        });
    }

    private void updateBleRegister() {
        BleAdvertiseRegister.getInstance().updateDatabaseState(currentMessageCount.get(), getHexHash());
    }

    private String getHexHash() {
        return String.format("%016X", currentGlobalHash.get());
    }

    private long generate64BitHash(String text) {
        long hash = -3750763034362895579L;
        for (int i = 0; i < text.length(); i++) {
            hash ^= text.charAt(i);
            hash *= 1099511628211L;
        }
        return hash;
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        dbExecutor.shutdown();
    }
}
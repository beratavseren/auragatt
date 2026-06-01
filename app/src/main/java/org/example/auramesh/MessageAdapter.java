package org.example.auramesh;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.example.auramesh.data.models.AuraMessage;
import org.example.auramesh.utils.AuraIdentityManager;
import android.graphics.drawable.GradientDrawable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<AuraMessage> messageList;
    private final String currentUserId;
    private final SimpleDateFormat timeFormat;

    // Context ekledik ki kendi ID'mizi alabilelim
    public MessageAdapter(Context context, List<AuraMessage> messageList) {
        this.messageList = messageList;
        this.currentUserId = AuraIdentityManager.getMyNodeId(context);
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        AuraMessage msg = messageList.get(position);

        // Mesajın göndericisi biz miyiz?
        boolean isMe = msg.senderUuid != null && msg.senderUuid.equals(currentUserId);

        holder.txtContent.setText(msg.payload);

        // Saati formatlayıp yazdırıyoruz
        String timeStr = timeFormat.format(new Date(msg.timestamp));
        holder.txtTime.setText(timeStr);

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);

        if (isMe) {
            // BİZİM GÖNDERDİĞİMİZ MESAJ (Sağa Yasla)
            holder.layoutWrapper.setGravity(Gravity.END);
            holder.txtSenderName.setVisibility(View.GONE);

            // Renk ayarlamaları (Tasarımına uygun hex kodları)
            int color;
            if (msg.payload != null && msg.payload.toUpperCase().startsWith("ENKAZ")) {
                color = Color.parseColor("#A10000"); // Koyu Kırmızı
            } else if (msg.payload != null && msg.payload.toUpperCase().startsWith("YARAL")) {
                color = Color.parseColor("#BD550E"); // Tasarımdaki Turuncu
            } else if (msg.payload != null && msg.payload.toUpperCase().startsWith("GÜVEN")) {
                color = Color.parseColor("#3F481B"); // Tasarımdaki Yeşil
            } else {
                color = Color.parseColor("#98AE41"); // Standart kendi mesajlarımız için yeşil
            }
            shape.setColor(color);

            // SAĞ ÜST köşeyi 0 yaparak sivri bırakıyoruz (Çıkıntı efekti)
            // float[] sırası: sol-üst, sağ-üst, sağ-alt, sol-alt
            shape.setCornerRadii(new float[]{30f, 30f, 0f, 0f, 30f, 30f, 30f, 30f});

        } else {
            // DIŞARIDAN GELEN MESAJ (Sola Yasla)
            holder.layoutWrapper.setGravity(Gravity.START);

            holder.txtSenderName.setVisibility(View.VISIBLE);
            holder.txtSenderName.setText("Unknown");

            shape.setColor(Color.parseColor("#323232")); // Tasarımdaki koyu gri

            // SOL ÜST köşeyi 0 yaparak sivri bırakıyoruz (Çıkıntı efekti)
            shape.setCornerRadii(new float[]{0f, 0f, 30f, 30f, 30f, 30f, 30f, 30f});
        }

        holder.layoutBubble.setBackground(shape);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutWrapper;
        LinearLayout layoutBubble;
        TextView txtSenderName;
        TextView txtContent;
        TextView txtTime;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutWrapper = itemView.findViewById(R.id.layoutMessageWrapper);
            layoutBubble = itemView.findViewById(R.id.layoutBubble);
            txtSenderName = itemView.findViewById(R.id.txtSenderName);
            txtContent = itemView.findViewById(R.id.txtMessageContent);
            txtTime = itemView.findViewById(R.id.txtMessageTime);
        }
    }
}
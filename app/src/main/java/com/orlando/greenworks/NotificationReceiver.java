package com.orlando.greenworks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

/**
 * The NotificationReceiver class extends the BroadcastReceiver class.
 * It represents a receiver for broadcast intents sent by the system or other applications.
 * The receiver creates a notification channel and displays a notification when it receives an intent.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "com.orlando.greenworks.notification.channel";

    /**
     * This method is called when the receiver has received an Intent broadcast.
     * It creates a notification channel and displays a notification.
     * @param context The Context in which the receiver is running.
     * @param intent The Intent received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Pickup Reminder")
                .setContentText("Don't forget your pickup tomorrow!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    /**
     * This method creates a notification channel.
     * It is required for notifications on Android Oreo and higher.
     * @param context The application context.
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Pickup Reminder";
            String description = "Channel for pickup reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
package com.orlando.greenworks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
        public static final String CHANNEL_ID = "notification_id";
        public static final String CHANNEL_NAME = "com.orlando.greenworks";
        public static final String CHANNEL_DESCRIPTION = "Channel for Greenworks notifications";

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
                if (remoteMessage.getNotification() != null) {
                        generateNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                }
        }

        private RemoteViews getRemoteView(String title, String message) {
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notifications);
                remoteViews.setTextViewText(R.id.title, title);
                remoteViews.setTextViewText(R.id.message, message);
                remoteViews.setImageViewResource(R.id.app_logo, R.drawable.logo);
                return remoteViews;
        }

        private void generateNotification(String title, String message) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000, 1000})
                        .setOnlyAlertOnce(true)
                        .setContentIntent(pendingIntent);

                builder.setContent(getRemoteView(title, message));

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                        channel.setDescription(CHANNEL_DESCRIPTION);
                        notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                }
                notificationManager.notify(0, builder.build());
        }
}
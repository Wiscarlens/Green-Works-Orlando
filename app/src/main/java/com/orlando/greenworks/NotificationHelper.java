package com.orlando.greenworks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * The NotificationHelper class provides methods to create a notification channel and display notifications.
 * It is used to send notifications to the user about the app's activities.
 */
public class NotificationHelper {

    private static final String CHANNEL_ID = "GREENWORKS_CHANNEL_ID";
    private static final String CHANNEL_NAME = "Greenworks Notifications";
    private static final String CHANNEL_DESC = "Notifications for Greenworks app";


    /**
     * This method creates a notification channel.
     * It is required for notifications on Android Oreo and higher.
     * @param context The application context.
     */
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }


    /**
     * This method displays a notification with the given title and body.
     * It sets the small icon, content title, content text, and priority of the notification.
     * It also sends the notification using the notification manager.
     * @param context The application context.
     * @param title The title of the notification.
     * @param body The body text of the notification.
     */
    public static void displayNotification(Context context, String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        try {
            managerCompat.notify(1, builder.build());
        } catch (SecurityException e) {
            // Log the exception or show a message to the user
        }
    }
}
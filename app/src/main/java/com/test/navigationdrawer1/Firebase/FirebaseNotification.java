package com.test.navigationdrawer1.Firebase;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by osvaldo on 11/5/17.
 */

public class FirebaseNotification extends FirebaseMessagingService {
    private static final String LOGTAG = ".MainActivity";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if( remoteMessage.getNotification() != null){
            String titulo = remoteMessage.getNotification().getTitle() ;
            String texto = remoteMessage.getNotification().getBody();
            showNotification(titulo, texto);
        }
    }

    private void showNotification(String titulo, String texto) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(android.R.drawable.stat_sys_warning).setContentTitle(titulo).setContentText(texto);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }


}

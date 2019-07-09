package com.lowlevelprog.lowlevelprogrammer.BroadcastReceiver;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lowlevelprog.lowlevelprogrammer.OnlineHelper;

public class FirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        handleNotification(remoteMessage.getNotification().getBody());
    }

    private void handleNotification(String body) {
        Intent pushNotification = new Intent(OnlineHelper.STR_PUSH);
        pushNotification.putExtra("message", body);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }
}

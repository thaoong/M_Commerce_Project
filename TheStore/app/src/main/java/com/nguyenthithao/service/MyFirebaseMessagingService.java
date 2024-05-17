package com.nguyenthithao.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nguyenthithao.model.Notification;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        DatabaseReference clientTokenRef = FirebaseDatabase.getInstance().getReference("clientTokens");
        clientTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean exists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String clientToken = snapshot.getValue(String.class);
                    if (clientToken.equals(token)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    clientTokenRef.push().setValue(token);
                }
                else {
                    Log.v(TAG, "Token existed: " + token);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error when add token: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Kiểm tra xem message có dữ liệu tùy chỉnh hay không
//        if (remoteMessage.getData().size() > 0) {
            // Lấy nội dung message và title từ dữ liệu tùy chỉnh
            String messageText = remoteMessage.getData().get("message");
            String messageTitle = remoteMessage.getData().get("title");

            // Lưu message vào Firebase Realtime Database
            saveMessageToDatabase(messageTitle, messageText);
//        } else if (remoteMessage.getNotification() != null) {
//            // Lấy nội dung message và title từ thông báo
//            String messageText = remoteMessage.getNotification().getBody();
//            String messageTitle = remoteMessage.getNotification().getTitle();
//
//            // Lưu message vào Firebase Realtime Database
//            saveMessageToDatabase(messageTitle, messageText);
//        }
    }

    private void saveMessageToDatabase(String messageTitle, String messageText) {
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("notifications");
        Notification notification = new Notification(messageTitle, messageText, "17/05/2024");

        // Lưu message vào database
        notificationRef.push().setValue(notification);
    }

}

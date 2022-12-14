package cz.mendelu.firebasepushnotification

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.moshi.Moshi
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        pushNotification(message.data)?.let { pushNotification ->
            NotificationManagerCompat.from(this).sendNotification(pushNotification, this)
        }
    }

    private fun pushNotification(data: Map<String, String>): PushNotification? {
        val moshi = Moshi.Builder().build()
        return moshi.adapter(PushNotification::class.java).fromJson(JSONObject(data).toString())
    }

    private fun sendToServer(token: String) {
        Log.d("MENDELU", "sendToServer: $token")
        // TODO send token to your backend
    }
}
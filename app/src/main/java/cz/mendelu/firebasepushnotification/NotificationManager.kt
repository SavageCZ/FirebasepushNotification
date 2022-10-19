package cz.mendelu.firebasepushnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.net.HttpURLConnection
import java.net.URL

@SuppressLint("MissingPermission")
fun NotificationManagerCompat.sendNotification(
    pushNotification: PushNotification,
    context: Context,
) {
    if (pushNotification.name == null || pushNotification.message == null) return

    val contentPendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            MainActivity.newIntent(context),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    val notification = NotificationCompat.Builder(
        context,
        context.getString(R.string.notification_channel_id_test)
    )
        .setSmallIcon(R.drawable.ic_message)
        .setContentTitle(pushNotification.name)
        .setContentText(pushNotification.message)
        .setLargeIcon(getBitmapFromURL(pushNotification.photoUrl))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()
    // other options: actions, large icon, ongoing notification, style...

    if (areNotificationsEnabled()) {
        notify(12, notification)
    }
}

fun Context.createNotificationChannel(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableVibration(true)
            enableLights(true)
            lightColor = Color.GREEN
        }

        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }
}

private fun getBitmapFromURL(src: String?): Bitmap? {
    return try {
        val input = with(URL(src).openConnection() as HttpURLConnection) {
            doInput = true
            connect()
            inputStream
        }
        BitmapFactory.decodeStream(input)
    } catch (e: Exception) {
        Log.e("MENDELU", "Bitmap download failed with error: ${e.message}")
        null
    }
}
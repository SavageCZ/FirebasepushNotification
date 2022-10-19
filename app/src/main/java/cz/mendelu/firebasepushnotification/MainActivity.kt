package cz.mendelu.firebasepushnotification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import cz.mendelu.firebasepushnotification.ui.theme.FirebasepushNotificationTheme

class MainActivity : ComponentActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If you want to retrieve current registration token
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            val token = it.result
            Log.d("MENDELU", "Token: $token")
        }

        createNotificationChannel(
            channelId = getString(R.string.notification_channel_id_test),
            channelName = getString(R.string.notification_channel_name_test),
        )

        val initialNotificationsEnabled =
            NotificationManagerCompat.from(this).areNotificationsEnabled()

        setContent {
            FirebasepushNotificationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var notificationsEnabled by remember {
                        mutableStateOf(initialNotificationsEnabled)
                    }

                    val permissionsRequest = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { granted ->
                            notificationsEnabled = granted
                        }
                    )

                    MainScreen(
                        notificationEnabled = notificationsEnabled,
                        onAllowNotification = {
                            permissionsRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                        },
                    )
                }
            }
        }
    }
}
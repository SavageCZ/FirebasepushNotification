package cz.mendelu.firebasepushnotification

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PushNotification(
    @Json(name = "message_text") val message: String?,
    @Json(name = "user_name") val name: String?,
    @Json(name = "user_photo") val photoUrl: String?,
)

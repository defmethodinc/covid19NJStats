package com.jesusmar.covid19njstats.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.jesusmar.covid19njstats.R


class Covid19FireBase : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        ChannelRegister().register(token)
    }


    companion object {
        fun setChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.channel_name)
                val descriptionText = context.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel(
                    context.getString(R.string.default_notification_channel_id),
                    name,
                    importance)
                mChannel.description = descriptionText
                val notificationManager =
                    context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }
}
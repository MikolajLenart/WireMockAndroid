package com.example.wiremockandroid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

val WILDCARD = ".*"
val rootFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "wiremockAndroid").apply {
    this.mkdirs()
}
val rootDirectory = rootFile.absolutePath
val mappingDirectory = File(rootFile, "mappings").apply {
    this.mkdirs()
}
val fileDirectory = File(rootFile, "__files").apply {
    this.mkdirs()
}

fun getNotification(context: Context, title: String, message: String): Notification {
    createChannel(context)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                .setContentText(message).build()
    } else {
        Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                .setContentText(message).build()

    }
}

private fun createChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

const val CHANNEL_ID = "example.com.WIREMOCK_CHANNEL_ID"
const val CHANNEL_NAME = "Wiremock Notification"

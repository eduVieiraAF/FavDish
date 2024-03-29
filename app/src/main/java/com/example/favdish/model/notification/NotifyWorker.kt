package com.example.favdish.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.favdish.R
import com.example.favdish.utils.Constants
import com.example.favdish.utils.vectorToBitmap
import com.example.favdish.view.activities.MainActivity

class NotifyWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.i("Notify Worker", "doWork function is called...")
        sendNotification()

        return Result.success()
    }

    private fun sendNotification() {
        val notificationId = 0

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.NOTIFICATION_ID, notificationId)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_all_dishes)
        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL)
            .setContentTitle(titleNotification)
            .setContentText(subtitleNotification)
            .setSmallIcon(R.drawable.ic_all_dishes)
            .setLargeIcon(bitmap)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setStyle(bigPicStyle)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(Constants.NOTIFICATION_CHANNEL)

            val ringtoneManager =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL,
                    Constants.NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.setSound(ringtoneManager, audioAttributes)
            //channel.vibrationPattern
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notification.build())
    }
}
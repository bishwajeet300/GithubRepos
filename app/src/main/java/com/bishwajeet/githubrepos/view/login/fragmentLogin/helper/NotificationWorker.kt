package com.bishwajeet.githubrepos.view.login.fragmentLogin.helper

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bishwajeet.githubrepos.BuildConfig
import com.bishwajeet.githubrepos.R

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        val notification = getOngoingNotification(
            applicationContext,
            "Update Repository",
            "Tap to get latest GitHub repository"
        )
        val mNotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(1000, notification)

        return Result.success()
    }


    private fun getOngoingNotification(
        context: Context,
        notificationTicker: String,
        notificationBody: String
    ): Notification? {
        val notification: Notification
        val pendingIntent =
            PendingIntent.getActivity(context, 1000, Intent(), PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            context,
            BuildConfig.APPLICATION_ID
        )
            .setSmallIcon(R.drawable.ic_git_branch)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(notificationBody) //Optional fields
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setTicker(notificationTicker)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setWhen(System.currentTimeMillis())
            .setOngoing(false)
            .setOnlyAlertOnce(true)
        notificationBuilder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(notificationBody)
                .setBigContentTitle(context.getString(R.string.app_name))
        )
        return try {
            notification = notificationBuilder.build()
            notification
        } catch (e: Exception) {
            null
        }
    }
}
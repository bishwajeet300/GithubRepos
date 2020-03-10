package com.bishwajeet.githubrepos

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import com.bishwajeet.githubrepos.di.ApplicationComponent
import com.bishwajeet.githubrepos.di.DaggerApplicationComponent
import java.util.*


class GitHubRepoApp : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        initializeComponent()
    }


    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }


    private fun initializeComponent(): ApplicationComponent {
        // Creates an instance of ApplicationComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerApplicationComponent.factory().create(applicationContext)
    }


    private fun createNotificationChannel() {

        val channelAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .build()

        val primaryChannel = NotificationChannel(
            BuildConfig.APPLICATION_ID,
            "General Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        primaryChannel.lightColor = Color.GREEN
        primaryChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        primaryChannel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            channelAttributes
        )
        primaryChannel.lightColor = Color.RED
        primaryChannel.enableVibration(true)
        (Objects.requireNonNull(getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager).createNotificationChannel(
            primaryChannel
        )
    }
}
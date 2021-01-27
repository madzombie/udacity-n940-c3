package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var radioGroup: RadioGroup



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        radioGroup = findViewById(R.id.radio_group)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        notificationManager = ContextCompat.getSystemService(this,NotificationManager::class.java) as NotificationManager
        createChannel()

        custom_button.setOnClickListener {
            custom_button.setState(ButtonState.Clicked)
            if (radioGroup.checkedRadioButtonId<0) {
                custom_button.setState(ButtonState.Completed)
                Toast.makeText(this, "Need to be select a file for download" ,Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Download Stating...." ,Toast.LENGTH_LONG).show()
                val radioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                download(radioButton.text.toString())

            }
        }

    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            notificationManager.cancelAll()
            val contentIntent = Intent(applicationContext, DetailActivity::class.java)
            contentIntent.putExtra("id","")
            contentIntent.putExtra("url","")


            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                downloadID.toInt(),
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID==id) {
                Toast.makeText(context, "Download Finished !!" ,Toast.LENGTH_LONG).show()
                Log.d("NotificationDownload","OK")

                notificationManager.sendNotification(R.string.notification_description.toString(),applicationContext,pendingIntent)
            } else {
                notificationManager.sendNotification("Download Failed",applicationContext,pendingIntent)
            }
            custom_button.setState(ButtonState.Completed)
        }
    }

    private fun download(url:String) {
        url.let {
            custom_button.setState(ButtonState.Loading)

            val request =
                DownloadManager.Request(Uri.parse("$url/archive/master.zip"))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID =R.string.channelId.toShort()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("NotCreate","OK")
            val notificationChannel = NotificationChannel(
                getString(R.string.channelId),
                    getString(R.string.channelName),
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.downloadedFile)
            notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            Log.d("NotCreate","ERROR")
        }
    }




}

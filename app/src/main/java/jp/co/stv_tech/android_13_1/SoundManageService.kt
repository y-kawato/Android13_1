package jp.co.stv_tech.android_13_1

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.lang.IllegalArgumentException

class SoundManageService : Service() {

    private var _player: MediaPlayer? = null

    override fun onCreate() {
        Log.i("SoundManageService", "onCreate")
        _player = MediaPlayer()

        val id ="soundmanagerservice_notification_channel"
        val name = getString(R.string.notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("SoundManageService", "onStartCommand")

        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.spring_mountain1}"

        val mediaFileUri = Uri.parse(mediaFileUriStr)
        try {
            _player?.setDataSource(applicationContext, mediaFileUri)
            _player?.setOnPreparedListener(PlayerPreparedListener())
            _player?.setOnCompletionListener(PlayCompletionListener())
            _player?.prepareAsync()
        }
        catch(ex: IllegalArgumentException) {
            Log.e("ServiceSample", "メディアプレーヤー準備時の例外発生", ex)

        }
        catch (ex: IOException) {
            Log.e("ServiceSample", "メディアプレーヤー準備時の例外発生", ex)
        }
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        _player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            _player = null
        }
    }
    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer?) {
            mp?.start()

            val builder = NotificationCompat.Builder(applicationContext, "soundmanagerservice_notification_channel")
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            builder.setContentTitle(getString(R.string.msg_notification_title_start))
            builder.setContentText(getString(R.string.msg_notification_text_start))

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("fromNotification", true)

            val stopServiceIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            builder.setContentIntent(stopServiceIntent)
            builder.setAutoCancel(true)

            val notification = builder.build()
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1, notification)
        }
    }
    private inner class PlayCompletionListener : MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer?) {

            Log.i("SoundManageService", "onCompletion")

            val builder = NotificationCompat.Builder(applicationContext, "soundmanagerservice_notification_channel")
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            builder.setContentTitle(getString(R.string.msg_notification_title_finish))
            builder.setContentText(getString(R.string.msg_notification_text_finish))
            val notification = builder.build()
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(0, notification)


            stopSelf()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")


    }
}

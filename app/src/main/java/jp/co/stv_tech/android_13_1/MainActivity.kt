package jp.co.stv_tech.android_13_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.i("MainActivity", "onCreate")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fromNotification = intent.getBooleanExtra("fromNotification", false)

        Log.i("MainActivity", "fromNotification")

        if(fromNotification) {
            Log.i("MainActivity", "if")
            val btPlay = findViewById<Button>(R.id.btPlay)
            val btStop = findViewById<Button>(R.id.btStop)
            btPlay.isEnabled = false
            btStop.isEnabled = true
        }
    }
    fun onPlayButtonClick(view: View) {
        Log.i("MainActivity", "onPlayButtonClick")

        val intent = Intent(applicationContext, SoundManageService::class.java)
        startService(intent)

        val btPlay = findViewById<Button>(R.id.btPlay)
        val btStop = findViewById<Button>(R.id.btStop)
        btPlay.isEnabled = false
        btStop.isEnabled = true
    }
    fun onStopButtonClick(view: View) {
        Log.i("MainActivity", "onStopButtonClick")
        val intent = Intent(applicationContext, SoundManageService::class.java)
        stopService(intent)

        val btPlay = findViewById<Button>(R.id.btPlay)
        val btStop = findViewById<Button>(R.id.btStop)
        btPlay.isEnabled = true
        btStop.isEnabled = false
    }
}

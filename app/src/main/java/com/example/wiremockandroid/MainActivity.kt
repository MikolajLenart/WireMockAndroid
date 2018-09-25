package com.example.wiremockandroid

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arePermissionsGranted()
        startRecordingButton.setOnClickListener {
            if (arePermissionsGranted()) {
                turnOffService(WiremockPlaybackService::class.java)
                WiremockRecordingService.startService(this, urlInput.text.toString())
            }
        }
        startPlaybackButton.setOnClickListener {
            if (arePermissionsGranted()) {
                turnOffService(WiremockRecordingService::class.java)
                val intent = Intent(this, WiremockPlaybackService::class.java)
                startService(intent)
            }
        }
        stopButton.setOnClickListener {
            turnOffService(WiremockRecordingService::class.java)
            turnOffService(WiremockPlaybackService::class.java)
        }
    }

    private fun arePermissionsGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            return false
        }
        return true
    }

    private fun turnOffService(service: Class<*>) {
        val intent = Intent(this, service)
        stopService(intent)
    }
}

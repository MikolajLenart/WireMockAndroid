package com.example.wiremockandroid

import android.app.Service
import android.content.Intent
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration

class WiremockPlaybackService : Service() {

    lateinit var wireMockServer: WireMockServer

    override fun onCreate() {
        super.onCreate()
        val notification = getNotification(this, "Wiremock", "Running in playback mode")
        startForeground(456, notification)
        wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(8080)
                .withRootDirectory(rootDirectory))
        wireMockServer.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        wireMockServer.stop()
        stopForeground(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

    override fun onBind(intent: Intent?) = null
}
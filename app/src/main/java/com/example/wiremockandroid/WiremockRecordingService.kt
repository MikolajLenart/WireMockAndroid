package com.example.wiremockandroid

import android.app.Service
import android.content.Context
import android.content.Intent
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.SingleRootFileSource
import com.github.tomakehurst.wiremock.core.WireMockConfiguration

class WiremockRecordingService() : Service() {

    lateinit var wireMockServer: WireMockServer

    override fun onCreate() {
        super.onCreate()
        val notification = getNotification(this, "Wiremock", "Running in recording mode")
        startForeground(123, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        wireMockServer.stop()
        stopForeground(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(remoteUrlTag)
        wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(8080)
                .withRootDirectory(rootDirectory))
        wireMockServer.enableRecordMappings(SingleRootFileSource(mappingDirectory), SingleRootFileSource(fileDirectory))
        wireMockServer.stubFor(WireMock.any(WireMock.urlMatching(WILDCARD)).willReturn(WireMock.aResponse().proxiedFrom(url)))
        wireMockServer.start()
        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null

    companion object {

        val remoteUrlTag = "remoteUrlTag"

        fun startService(context: Context, remoteUrl: String) {
            val intent = Intent(context, WiremockRecordingService::class.java)
            intent.putExtra(remoteUrlTag, remoteUrl)
            context.startService(intent)
        }
    }
}
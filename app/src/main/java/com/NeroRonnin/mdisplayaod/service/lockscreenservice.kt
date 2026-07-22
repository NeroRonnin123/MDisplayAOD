package com.NeroRonnin.mdisplayaod.service

import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.NeroRonnin.mdisplayaod.R

class LockScreenService : Service() {


    private val screenReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            when (intent?.action) {

                Intent.ACTION_SCREEN_OFF -> {
                    Log.d(
                        "MDisplayAOD_LOCK",
                        "Pantalla APAGADA"
                    )

                    android.os.Handler(
                        android.os.Looper.getMainLooper()
                    ).postDelayed({

                        val keyguardManager =
                            getSystemService(KEYGUARD_SERVICE) as KeyguardManager

                        Log.d(
                            "MDisplayAOD_LOCK",
                            "Intentando mostrar AOD después de SCREEN_OFF"
                        )

                        if (keyguardManager.isKeyguardLocked) {

                            val intent = Intent(
                                this@LockScreenService,
                                com.NeroRonnin.mdisplayaod.MainActivity::class.java
                            ).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            }

                            try {

                                startActivity(intent)

                                Log.d(
                                    "MDisplayAOD_LOCK",
                                    "Intent AOD enviado"
                                )

                            } catch (e: Exception) {

                                Log.e(
                                    "MDisplayAOD_LOCK",
                                    "Error mostrando AOD",
                                    e
                                )
                            }
                        }

                    }, 500)
                }

                Intent.ACTION_SCREEN_ON -> {
                    Log.d("MDisplayAOD_LOCK", "Pantalla ENCENDIDA")
                }

                Intent.ACTION_USER_PRESENT -> {

                    Log.d(
                        "MDisplayAOD_LOCK",
                        "Usuario DESBLOQUEÓ"
                    )


                }
            }
        }
    }


    companion object {
        private const val CHANNEL_ID = "mdisplayaod_service"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()


        createNotificationChannel()

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
        }

        registerReceiver(
            screenReceiver,
            filter
        )

        Log.d(
            "MDisplayAOD_LOCK",
            "ScreenReceiver registrado"
        )
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        val notification = createNotification()

        startForeground(
            NOTIFICATION_ID,
            notification
        )

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            CHANNEL_ID,
            "MDisplayAOD",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Mantiene activa la pantalla personalizada"
        }

        val manager =
            getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {

        return NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setContentTitle("MDisplayAOD activo")
            .setContentText("Pantalla de bloqueo personalizada habilitada")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {

        unregisterReceiver(screenReceiver)

        Log.d(
            "MDisplayAOD_LOCK",
            "LockScreenService destruido"
        )

        super.onDestroy()
    }



}
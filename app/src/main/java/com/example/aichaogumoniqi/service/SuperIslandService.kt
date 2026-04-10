package com.example.aichaogumoniqi.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.aichaogumoniqi.MainActivity
import com.example.aichaogumoniqi.R
import kotlinx.coroutines.*
import org.json.JSONObject

class SuperIslandService : Service() {
    
    companion object {
        private const val TAG = "SuperIslandService"
        private const val CHANNEL_ID = "stock_super_island"
        private const val NOTIFICATION_ID = 1001
        private const val UPDATE_INTERVAL = 2000L // 2秒更新一次
        
        fun startService(context: Context) {
            val intent = Intent(context, SuperIslandService::class.java)
            context.startForegroundService(intent)
        }
        
        fun stopService(context: Context) {
            val intent = Intent(context, SuperIslandService::class.java)
            context.stopService(intent)
        }
    }
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var updateJob: Job? = null
    private var notificationManager: NotificationManager? = null
    private var isNetworkAvailable = true
    
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        Log.d(TAG, "SuperIslandService created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "SuperIslandService started")
        startForeground(NOTIFICATION_ID, createNotification())
        startUpdateLoop()
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        updateJob?.cancel()
        serviceScope.cancel()
        Log.d(TAG, "SuperIslandService destroyed")
    }
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "股票超级岛",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "显示实时股票盈亏信息"
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager?.createNotificationChannel(channel)
    }
    
    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AI炒股模拟器")
            .setContentText("正在监控市场...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
    
    private fun startUpdateLoop() {
        updateJob = serviceScope.launch {
            while (isActive) {
                try {
                    if (isNetworkAvailable) {
                        updateSuperIsland()
                    } else {
                        showNetworkError()
                        break
                    }
                    delay(UPDATE_INTERVAL)
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating super island", e)
                    delay(UPDATE_INTERVAL)
                }
            }
        }
    }
    
    private suspend fun updateSuperIsland() {
        withContext(Dispatchers.Main) {
            try {
                // 获取实时盈亏数据
                val totalProfitLoss = getTotalProfitLoss()
                val isProfit = totalProfitLoss >= 0
                val profitText = if (isProfit) "+${String.format("%.2f", totalProfitLoss)}" 
                                else String.format("%.2f", totalProfitLoss)
                
                // 创建小米超级岛通知
                val notification = createSuperIslandNotification(profitText, isProfit)
                notificationManager?.notify(NOTIFICATION_ID, notification)
                
                Log.d(TAG, "SuperIsland updated: $profitText")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating super island notification", e)
            }
        }
    }
    
    private fun createSuperIslandNotification(profitText: String, isProfit: Boolean): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // 创建小米超级岛扩展参数
        val focusParam = createFocusParam(profitText, isProfit)
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("实时盈亏")
            .setContentText(profitText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setExtras(android.os.Bundle().apply {
                putString("miui.focus.param", focusParam.toString())
            })
            .build()
    }
    
    private fun createFocusParam(profitText: String, isProfit: Boolean): JSONObject {
        return JSONObject().apply {
            // 摘要态
            put("summary", JSONObject().apply {
                put("title", "AI炒股模拟器")
                put("content", "实时盈亏: $profitText")
                put("icon", R.mipmap.ic_launcher)
            })
            
            // 焦点通知
            put("focus", JSONObject().apply {
                put("title", "实时盈亏")
                put("content", profitText)
                put("color", if (isProfit) "#FF0000" else "#00FF00")
                put("ticker", profitText)
                put("tickerPic", if (isProfit) "arrow_up" else "arrow_down")
            })
            
            // 交互能力
            put("interaction", JSONObject().apply {
                put("click", JSONObject().apply {
                    put("action", "open_app")
                })
                put("longPress", JSONObject().apply {
                    put("action", "stop_service")
                })
            })
            
            // 息屏显示
            put("aodTitle", "AI炒股")
            put("aodPic", R.mipmap.ic_launcher)
            
            // 状态栏显示
            put("ticker", profitText)
            put("tickerPic", if (isProfit) "arrow_up" else "arrow_down")
        }
    }
    
    private fun showNetworkError() {
        serviceScope.launch(Dispatchers.Main) {
            val notification = NotificationCompat.Builder(this@SuperIslandService, CHANNEL_ID)
                .setContentTitle("网络错误")
                .setContentText("网络连接已断开，超级岛已关闭")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
            
            notificationManager?.notify(NOTIFICATION_ID, notification)
            
            // 延迟后关闭服务
            delay(3000)
            stopSelf()
        }
    }
    
    private fun getTotalProfitLoss(): Double {
        // 这里应该从数据库或API获取实际数据
        // 暂时返回模拟数据
        return 2345.67
    }
    
    fun setNetworkAvailable(available: Boolean) {
        isNetworkAvailable = available
    }
}
package com.example.aichaogumoniqi.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            "android.intent.action.QUICKBOOT_POWERON" -> {
                Log.d(TAG, "Boot completed, starting services")
                startServices(context)
            }
        }
    }
    
    private fun startServices(context: Context) {
        try {
            // 启动超级岛服务
            SuperIslandService.startService(context)
            Log.d(TAG, "SuperIslandService started")
            
            // 启动MCP服务
            MCPServer.startService(context)
            Log.d(TAG, "MCPServer started")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error starting services", e)
        }
    }
}
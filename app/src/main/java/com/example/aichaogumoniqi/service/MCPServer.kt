package com.example.aichaogumoniqi.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import java.io.*
import java.net.ServerSocket
import java.net.Socket

class MCPServer : Service() {
    
    companion object {
        private const val TAG = "MCPServer"
        private const val DEFAULT_PORT = 8080
        
        fun startService(context: android.content.Context) {
            val intent = Intent(context, MCPServer::class.java)
            context.startService(intent)
        }
        
        fun stopService(context: android.content.Context) {
            val intent = Intent(context, MCPServer::class.java)
            context.stopService(intent)
        }
    }
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var serverSocket: ServerSocket? = null
    private var isRunning = false
    private val gson = Gson()
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MCPServer created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val port = intent?.getIntExtra("port", DEFAULT_PORT) ?: DEFAULT_PORT
        startServer(port)
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopServer()
        Log.d(TAG, "MCPServer destroyed")
    }
    
    private fun startServer(port: Int) {
        serviceScope.launch {
            try {
                serverSocket = ServerSocket(port)
                isRunning = true
                Log.d(TAG, "MCP Server started on port $port")
                
                while (isRunning) {
                    try {
                        val clientSocket = serverSocket?.accept()
                        clientSocket?.let { handleClient(it) }
                    } catch (e: Exception) {
                        if (isRunning) {
                            Log.e(TAG, "Error accepting client", e)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting server", e)
            }
        }
    }
    
    private fun stopServer() {
        isRunning = false
        serviceScope.launch {
            try {
                serverSocket?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing server", e)
            }
        }
    }
    
    private fun handleClient(clientSocket: Socket) {
        serviceScope.launch {
            try {
                val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                val writer = PrintWriter(OutputStreamWriter(clientSocket.getOutputStream()), true)
                
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    try {
                        val request = JsonParser.parseString(line).asJsonObject
                        val response = processRequest(request)
                        writer.println(response.toString())
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing request", e)
                        val errorResponse = createErrorResponse("Invalid request", -32600)
                        writer.println(errorResponse.toString())
                    }
                }
                
                clientSocket.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error handling client", e)
            }
        }
    }
    
    private fun processRequest(request: JsonObject): JsonObject {
        val method = request.get("method")?.asString ?: ""
        val params = request.get("params")?.asJsonObject
        val id = request.get("id")
        
        return when (method) {
            "searchStock" -> handleSearchStock(params, id)
            "getStockTrend" -> handleGetStockTrend(params, id)
            "getPortfolio" -> handleGetPortfolio(params, id)
            "buyStock" -> handleBuyStock(params, id)
            "sellStock" -> handleSellStock(params, id)
            "cancelOrder" -> handleCancelOrder(params, id)
            "getAccountInfo" -> handleGetAccountInfo(params, id)
            "getTransactionHistory" -> handleGetTransactionHistory(params, id)
            "getRecommendations" -> handleGetRecommendations(params, id)
            "getWatchlist" -> handleGetWatchlist(params, id)
            "addToWatchlist" -> handleAddToWatchlist(params, id)
            "removeFromWatchlist" -> handleRemoveFromWatchlist(params, id)
            "getMarketStatus" -> handleGetMarketStatus(params, id)
            "getStockPrice" -> handleGetStockPrice(params, id)
            "getAccountBalance" -> handleGetAccountBalance(params, id)
            else -> createErrorResponse("Method not found", -32601, id)
        }
    }
    
    private fun handleSearchStock(params: JsonObject?, id: Any?): JsonObject {
        val keyword = params?.get("keyword")?.asString ?: ""
        // 这里应该调用实际的搜索API
        val results = listOf(
            mapOf("code" to "000001", "name" to "平安银行", "price" to 12.34, "change" to 2.5),
            mapOf("code" to "000002", "name" to "万科A", "price" to 18.56, "change" to -1.2)
        )
        return createSuccessResponse(results, id)
    }
    
    private fun handleGetStockTrend(params: JsonObject?, id: Any?): JsonObject {
        val code = params?.get("code")?.asString ?: ""
        val period = params?.get("period")?.asString ?: "1d"
        // 这里应该获取实际的趋势数据
        val trendData = listOf(12.34, 12.36, 12.35, 12.38, 12.40, 12.39, 12.42)
        return createSuccessResponse(mapOf("code" to code, "period" to period, "data" to trendData), id)
    }
    
    private fun handleGetPortfolio(params: JsonObject?, id: Any?): JsonObject {
        // 这里应该从数据库获取实际持仓
        val portfolio = listOf(
            mapOf("code" to "000001", "name" to "平安银行", "quantity" to 1000, "cost" to 12.00, "current" to 12.34),
            mapOf("code" to "600519", "name" to "贵州茅台", "quantity" to 100, "cost" to 1800.00, "current" to 1856.00)
        )
        return createSuccessResponse(portfolio, id)
    }
    
    private fun handleBuyStock(params: JsonObject?, id: Any?): JsonObject {
        val code = params?.get("code")?.asString ?: ""
        val quantity = params?.get("quantity")?.asInt ?: 0
        val price = params?.get("price")?.asDouble ?: 0.0
        val orderType = params?.get("orderType")?.asString ?: "limit"
        
        // 这里应该执行实际的买入逻辑
        val orderId = "BUY_${System.currentTimeMillis()}"
        val result = mapOf(
            "orderId" to orderId,
            "code" to code,
            "quantity" to quantity,
            "price" to price,
            "type" to orderType,
            "status" to "submitted",
            "timestamp" to System.currentTimeMillis()
        )
        
        Log.d(TAG, "Buy order: $result")
        return createSuccessResponse(result, id)
    }
    
    private fun handleSellStock(params: JsonObject?, id: Any?): JsonObject {
        val code = params?.get("code")?.asString ?: ""
        val quantity = params?.get("quantity")?.asInt ?: 0
        val price = params?.get("price")?.asDouble ?: 0.0
        val orderType = params?.get("orderType")?.asString ?: "limit"
        
        // 这里应该执行实际的卖出逻辑
        val orderId = "SELL_${System.currentTimeMillis()}"
        val result = mapOf(
            "orderId" to orderId,
            "code" to code,
            "quantity" to quantity,
            "price" to price,
            "type" to orderType,
            "status" to "submitted",
            "timestamp" to System.currentTimeMillis()
        )
        
        Log.d(TAG, "Sell order: $result")
        return createSuccessResponse(result, id)
    }
    
    private fun handleCancelOrder(params: JsonObject?, id: Any?): JsonObject {
        val orderId = params?.get("orderId")?.asString ?: ""
        // 这里应该执行实际的撤单逻辑
        val result = mapOf(
            "orderId" to orderId,
            "status" to "cancelled",
            "timestamp" to System.currentTimeMillis()
        )
        
        Log.d(TAG, "Cancel order: $result")
        return createSuccessResponse(result, id)
    }
    
    private fun handleGetAccountInfo(params: JsonObject?, id: Any?): JsonObject {
        // 这里应该从数据库获取实际账户信息
        val accountInfo = mapOf(
            "totalAssets" to 125680.00,
            "availableCash" to 25000.00,
            "marketValue" to 100680.00,
            "todayProfitLoss" to 2345.67,
            "totalProfitLoss" to 5680.00,
            "profitRate" to 4.5
        )
        return createSuccessResponse(accountInfo, id)
    }
    
    private fun handleGetTransactionHistory(params: JsonObject?, id: Any?): JsonObject {
        val limit = params?.get("limit")?.asInt ?: 10
        val offset = params?.get("offset")?.asInt ?: 0
        
        // 这里应该从数据库获取实际交易记录
        val history = listOf(
            mapOf(
                "id" to 1,
                "type" to "buy",
                "code" to "000001",
                "name" to "平安银行",
                "quantity" to 1000,
                "price" to 12.00,
                "amount" to 12000.00,
                "timestamp" to System.currentTimeMillis() - 86400000
            ),
            mapOf(
                "id" to 2,
                "type" to "sell",
                "code" to "000002",
                "name" to "万科A",
                "quantity" to 500,
                "price" to 18.50,
                "amount" to 9250.00,
                "timestamp" to System.currentTimeMillis()
            )
        )
        
        return createSuccessResponse(mapOf("history" to history, "total" to history.size), id)
    }
    
    private fun handleGetRecommendations(params: JsonObject?, id: Any?): JsonObject {
        val riskLevel = params?.get("riskLevel")?.asString ?: "medium"
        val count = params?.get("count")?.asInt ?: 10
        
        // 这里应该调用AI推荐算法
        val recommendations = listOf(
            mapOf("code" to "000001", "name" to "平安银行", "reason" to "近期涨幅稳定", "risk" to "low"),
            mapOf("code" to "600519", "name" to "贵州茅台", "reason" to "基本面优秀", "risk" to "low"),
            mapOf("code" to "300750", "name" to "宁德时代", "reason" to "新能源龙头", "risk" to "medium")
        )
        
        return createSuccessResponse(recommendations, id)
    }
    
    private fun handleGetWatchlist(params: JsonObject?, id: Any?): JsonObject {
        // 这里应该从数据库获取实际关注列表
        val watchlist = listOf(
            mapOf("code" to "002594", "name" to "比亚迪", "price" to 265.80, "change" to -0.5),
            mapOf("code" to "601318", "name" to "中国平安", "price" to 48.76, "change" to 0.8)
        )
        return createSuccessResponse(watchlist, id)
    }
    
    private fun handleAddToWatchlist(params: JsonObject?, id: Any?): JsonObject {
        val code = params?.get("code")?.asString ?: ""
        // 这里应该添加到数据库
        val result = mapOf("code" to code, "status" to "added")
        Log.d(TAG, "Add to watchlist: $code")
        return createSuccessResponse(result, id)
    }
    
    private fun handleRemoveFromWatchlist(params: JsonObject?, id: Any?): JsonObject {
        val code = params?.get("code")?.asString ?: ""
        // 这里应该从数据库移除
        val result = mapOf("code" to code, "status" to "removed")
        Log.d(TAG, "Remove from watchlist: $code")
        return createSuccessResponse(result, id)
    }
    
    private fun handleGetMarketStatus(params: JsonObject?, id: Any?): JsonObject {
        val currentTime = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = currentTime
        
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = calendar.get(java.util.Calendar.MINUTE)
        val dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
        
        val isWeekday = dayOfWeek in 2..6 // 周一到周五
        val isInTradingHours = (hour == 9 && minute >= 30) || (hour == 10) || 
                              (hour == 11 && minute <= 30) || 
                              (hour in 13..14) || (hour == 15 && minute == 0)
        
        val isOpen = isWeekday && isInTradingHours
        
        return createSuccessResponse(mapOf(
            "isOpen" to isOpen,
            "currentTime" to currentTime,
            "tradingHours" to "9:30-11:30, 13:00-15:00",
            "timezone" to "Asia/Shanghai"
        ), id)
    }
    
    private fun handleGetStockPrice(params: JsonObject?, id: Any?): JsonObject {
        val code = params?.get("code")?.asString ?: ""
        // 这里应该调用实际的API获取价格
        val priceData = mapOf(
            "code" to code,
            "price" to 12.34,
            "open" to 12.30,
            "high" to 12.40,
            "low" to 12.25,
            "close" to 12.34,
            "volume" to 1000000,
            "amount" to 12340000.0,
            "change" to 0.04,
            "changePercent" to 0.33,
            "timestamp" to System.currentTimeMillis()
        )
        return createSuccessResponse(priceData, id)
    }
    
    private fun handleGetAccountBalance(params: JsonObject?, id: Any?): JsonObject {
        // 这里应该从数据库获取实际余额
        val balance = mapOf(
            "cash" to 25000.00,
            "frozen" to 0.00,
            "available" to 25000.00,
            "currency" to "CNY"
        )
        return createSuccessResponse(balance, id)
    }
    
    private fun createSuccessResponse(result: Any, id: Any?): JsonObject {
        return JsonObject().apply {
            addProperty("jsonrpc", "2.0")
            add("result", gson.toJsonTree(result))
            add("id", gson.toJsonTree(id))
        }
    }
    
    private fun createErrorResponse(message: String, code: Int, id: Any? = null): JsonObject {
        return JsonObject().apply {
            addProperty("jsonrpc", "2.0")
            add("error", JsonObject().apply {
                addProperty("code", code)
                addProperty("message", message)
            })
            add("id", gson.toJsonTree(id))
        }
    }
}
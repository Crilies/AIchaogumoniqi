package com.example.aichaogumoniqi.data.model

import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("open")
    val open: Double,
    
    @SerializedName("high")
    val high: Double,
    
    @SerializedName("low")
    val low: Double,
    
    @SerializedName("close")
    val close: Double,
    
    @SerializedName("volume")
    val volume: Long,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("change")
    val change: Double,
    
    @SerializedName("changePercent")
    val changePercent: Double,
    
    @SerializedName("timestamp")
    val timestamp: Long
)

data class StockRecommendation(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("change")
    val change: Double,
    
    @SerializedName("reason")
    val reason: String,
    
    @SerializedName("risk")
    val risk: String,
    
    @SerializedName("score")
    val score: Double = 0.0
)

data class PurchasedStock(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("costPrice")
    val costPrice: Double,
    
    @SerializedName("currentPrice")
    val currentPrice: Double,
    
    @SerializedName("profitLoss")
    val profitLoss: Double = (currentPrice - costPrice) * quantity,
    
    @SerializedName("profitLossPercent")
    val profitLossPercent: Double = ((currentPrice - costPrice) / costPrice) * 100
)

data class WatchedStock(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("change")
    val change: Double,
    
    @SerializedName("addedTime")
    val addedTime: Long = System.currentTimeMillis()
)

data class Transaction(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("type")
    val type: String, // "buy" or "sell"
    
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("fee")
    val fee: Double,
    
    @SerializedName("timestamp")
    val timestamp: Long,
    
    @SerializedName("status")
    val status: String = "completed" // "pending", "completed", "cancelled"
)

data class Account(
    @SerializedName("totalAssets")
    val totalAssets: Double,
    
    @SerializedName("availableCash")
    val availableCash: Double,
    
    @SerializedName("marketValue")
    val marketValue: Double,
    
    @SerializedName("todayProfitLoss")
    val todayProfitLoss: Double,
    
    @SerializedName("totalProfitLoss")
    val totalProfitLoss: Double,
    
    @SerializedName("profitRate")
    val profitRate: Double,
    
    @SerializedName("initialCapital")
    val initialCapital: Double = 1000000.0,
    
    @SerializedName("currency")
    val currency: String = "CNY"
)

data class MarketStatus(
    @SerializedName("isOpen")
    val isOpen: Boolean,
    
    @SerializedName("currentTime")
    val currentTime: Long,
    
    @SerializedName("tradingHours")
    val tradingHours: String,
    
    @SerializedName("timezone")
    val timezone: String
)

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: T?,
    
    @SerializedName("error")
    val error: String?,
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

data class MCPRequest(
    @SerializedName("jsonrpc")
    val jsonrpc: String = "2.0",
    
    @SerializedName("method")
    val method: String,
    
    @SerializedName("params")
    val params: Map<String, Any>?,
    
    @SerializedName("id")
    val id: Any?
)

data class MCPResponse(
    @SerializedName("jsonrpc")
    val jsonrpc: String = "2.0",
    
    @SerializedName("result")
    val result: Any?,
    
    @SerializedName("error")
    val error: MCPError?,
    
    @SerializedName("id")
    val id: Any?
)

data class MCPError(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: Any? = null
)
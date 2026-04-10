package com.example.aichaogumoniqi.data.repository

import com.example.aichaogumoniqi.data.api.StockApiService
import com.example.aichaogumoniqi.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StockRepository(private val apiService: StockApiService) {
    
    suspend fun getRecommendations(riskLevel: String = "medium", count: Int = 10): List<StockRecommendation> {
        return withContext(Dispatchers.IO) {
            try {
                // 这里应该调用实际的API
                // 暂时返回模拟数据
                getMockRecommendations(riskLevel, count)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    suspend fun getStockPrice(code: String): Stock? {
        return withContext(Dispatchers.IO) {
            try {
                // 这里应该调用实际的API
                // 暂时返回模拟数据
                getMockStockPrice(code)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    suspend fun getStockTrend(code: String, period: String = "1d"): List<Double> {
        return withContext(Dispatchers.IO) {
            try {
                // 这里应该调用实际的API
                // 暂时返回模拟数据
                getMockStockTrend(code, period)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    suspend fun searchStocks(keyword: String): List<Stock> {
        return withContext(Dispatchers.IO) {
            try {
                // 这里应该调用实际的API
                // 暂时返回模拟数据
                getMockSearchResults(keyword)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    suspend fun getMarketStatus(): MarketStatus {
        return withContext(Dispatchers.IO) {
            try {
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
                
                MarketStatus(
                    isOpen = isWeekday && isInTradingHours,
                    currentTime = currentTime,
                    tradingHours = "9:30-11:30, 13:00-15:00",
                    timezone = "Asia/Shanghai"
                )
            } catch (e: Exception) {
                MarketStatus(
                    isOpen = false,
                    currentTime = System.currentTimeMillis(),
                    tradingHours = "9:30-11:30, 13:00-15:00",
                    timezone = "Asia/Shanghai"
                )
            }
        }
    }
    
    private fun getMockRecommendations(riskLevel: String, count: Int): List<StockRecommendation> {
        val allStocks = listOf(
            StockRecommendation("000001", "平安银行", 12.34, 2.5, "近期涨幅稳定", "low", 85.5),
            StockRecommendation("600519", "贵州茅台", 1856.00, 1.8, "基本面优秀", "low", 90.2),
            StockRecommendation("300750", "宁德时代", 215.50, 3.2, "新能源龙头", "medium", 82.3),
            StockRecommendation("002594", "比亚迪", 265.80, -0.5, "新能源汽车龙头", "medium", 78.9),
            StockRecommendation("601318", "中国平安", 48.76, 0.8, "金融蓝筹", "low", 81.7),
            StockRecommendation("600036", "招商银行", 32.45, 1.5, "银行龙头", "low", 83.4),
            StockRecommendation("000858", "五粮液", 165.30, -2.1, "白酒龙头", "low", 79.8),
            StockRecommendation("601012", "隆基绿能", 25.80, 4.2, "光伏龙头", "high", 76.5),
            StockRecommendation("600030", "中信证券", 22.15, 0.3, "券商龙头", "medium", 77.2),
            StockRecommendation("000002", "万科A", 18.56, -1.2, "地产龙头", "high", 72.8),
            StockRecommendation("002475", "立讯精密", 32.15, 2.8, "消费电子", "medium", 79.3),
            StockRecommendation("300059", "东方财富", 16.78, 1.2, "互联网金融", "medium", 76.9),
            StockRecommendation("600276", "恒瑞医药", 45.60, 0.6, "医药龙头", "low", 84.1),
            StockRecommendation("000568", "泸州老窖", 185.20, -0.8, "白酒次龙头", "low", 80.5),
            StockRecommendation("600900", "长江电力", 22.35, 0.2, "公用事业", "low", 82.7)
        )
        
        return when (riskLevel) {
            "low" -> allStocks.filter { it.risk == "low" }.take(count)
            "high" -> allStocks.filter { it.risk == "high" }.take(count)
            else -> allStocks.take(count) // medium风险，返回所有
        }
    }
    
    private fun getMockStockPrice(code: String): Stock {
        val mockData = mapOf(
            "000001" to Stock("000001", "平安银行", 12.34, 12.30, 12.40, 12.25, 12.34, 1000000, 12340000.0, 0.04, 0.33, System.currentTimeMillis()),
            "600519" to Stock("600519", "贵州茅台", 1856.00, 1850.00, 1860.00, 1845.00, 1856.00, 50000, 92800000.0, 6.00, 0.32, System.currentTimeMillis()),
            "300750" to Stock("300750", "宁德时代", 215.50, 214.00, 216.50, 213.50, 215.50, 800000, 172400000.0, 1.50, 0.70, System.currentTimeMillis()),
            "002594" to Stock("002594", "比亚迪", 265.80, 266.00, 267.00, 264.50, 265.80, 600000, 159480000.0, -0.20, -0.08, System.currentTimeMillis())
        )
        
        return mockData[code] ?: Stock(code, "未知股票", 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0, 0.0, System.currentTimeMillis())
    }
    
    private fun getMockStockTrend(code: String, period: String): List<Double> {
        val basePrice = getMockStockPrice(code).price
        val volatility = when (period) {
            "1d" -> 0.02
            "1w" -> 0.05
            "1m" -> 0.10
            "6m" -> 0.20
            "1y" -> 0.30
            else -> 0.02
        }
        
        val dataPoints = when (period) {
            "1d" -> 24 // 24小时
            "1w" -> 7 // 7天
            "1m" -> 30 // 30天
            "6m" -> 180 // 180天
            "1y" -> 365 // 365天
            else -> 24
        }
        
        val random = java.util.Random()
        return (0 until dataPoints).map { i ->
            val change = (random.nextDouble() - 0.5) * 2 * volatility * basePrice
            basePrice + change * (i.toDouble() / dataPoints)
        }
    }
    
    private fun getMockSearchResults(keyword: String): List<Stock> {
        val allStocks = listOf(
            getMockStockPrice("000001"),
            getMockStockPrice("600519"),
            getMockStockPrice("300750"),
            getMockStockPrice("002594"),
            getMockStockPrice("601318"),
            getMockStockPrice("600036"),
            getMockStockPrice("000858"),
            getMockStockPrice("601012"),
            getMockStockPrice("600030"),
            getMockStockPrice("000002")
        )
        
        return allStocks.filter { 
            it.code.contains(keyword, ignoreCase = true) || 
            it.name.contains(keyword, ignoreCase = true) 
        }
    }
}
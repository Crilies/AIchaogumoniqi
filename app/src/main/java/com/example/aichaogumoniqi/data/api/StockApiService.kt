package com.example.aichaogumoniqi.data.api

import com.example.aichaogumoniqi.data.model.*
import retrofit2.http.*

interface StockApiService {
    
    // 新浪财经API
    @GET("https://hq.sinajs.cn/list={codes}")
    suspend fun getSinaStockPrices(@Path("codes") codes: String): String
    
    // 腾讯股票API
    @GET("https://qt.gtimg.cn/q={codes}")
    suspend fun getTencentStockPrices(@Path("codes") codes: String): String
    
    // 东方财富API
    @GET("https://push2.eastmoney.com/api/qt/stock/get")
    suspend fun getEastmoneyStockPrice(
        @Query("secid") secid: String,
        @Query("fields") fields: String = "f43,f44,f45,f46,f47,f48,f57,f58,f169,f170"
    ): String
    
    // 通用搜索接口
    @GET("https://searchapi.eastmoney.com/api/suggest/get")
    suspend fun searchStocks(
        @Query("input") input: String,
        @Query("type") type: String = "14",
        @Query("token") token: String = "D43BF722C8E33BDC906FB84D85E326E8"
    ): String
    
    // 历史数据接口
    @GET("https://push2his.eastmoney.com/api/qt/stock/kline/get")
    suspend fun getStockKline(
        @Query("secid") secid: String,
        @Query("fields1") fields1: String = "f1,f2,f3,f4,f5,f6",
        @Query("fields2") fields2: String = "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61",
        @Query("klt") klt: String = "101", // 101:日线, 102:周线, 103:月线
        @Query("fqt") fqt: String = "1",
        @Query("beg") beg: String = "0",
        @Query("end") end: String = "20500101"
    ): String
    
    // 实时行情接口
    @GET("https://push2.eastmoney.com/api/qt/stock/get")
    suspend fun getRealtimeQuote(
        @Query("secid") secid: String,
        @Query("ut") ut: String = "fa5fd1943c7b386f172d6893dbbd49a8",
        @Query("fields") fields: String = "f43,f44,f45,f46,f47,f48,f57,f58,f169,f170,f171"
    ): String
    
    // 基金净值接口
    @GET("https://fundgz.1702.com/js/{code}.js")
    suspend fun getFundNetValue(@Path("code") code: String): String
    
    // 贵金属行情接口
    @GET("https://api.exchangerate-api.com/v4/latest/XAU")
    suspend fun getGoldPrice(): String
    
    // 指数行情接口
    @GET("https://push2.eastmoney.com/api/qt/stock/get")
    suspend fun getIndexQuote(
        @Query("secid") secid: String, // 如: "1.000001" 表示上证指数
        @Query("fields") fields: String = "f43,f44,f45,f46,f47,f48"
    ): String
}

// API配置
object ApiConfig {
    // 新浪财经
    const val SINA_BASE_URL = "https://hq.sinajs.cn/"
    
    // 腾讯股票
    const val TENCENT_BASE_URL = "https://qt.gtimg.cn/"
    
    // 东方财富
    const val EASTMONEY_BASE_URL = "https://push2.eastmoney.com/"
    const val EASTMONEY_SEARCH_URL = "https://searchapi.eastmoney.com/"
    
    // 基金
    const val FUND_BASE_URL = "https://fundgz.1702.com/"
    
    // 贵金属
    const val GOLD_BASE_URL = "https://api.exchangerate-api.com/"
    
    // 默认超时时间
    const val TIMEOUT_DURATION = 30L
    
    // API密钥（如果需要）
    const val API_KEY = ""
}

// 数据源类型
enum class DataSource(val displayName: String, val baseUrl: String) {
    SINA("新浪财经", ApiConfig.SINA_BASE_URL),
    TENCENT("腾讯股票", ApiConfig.TENCENT_BASE_URL),
    EASTMONEY("东方财富", ApiConfig.EASTMONEY_BASE_URL),
    FUND("基金数据", ApiConfig.FUND_BASE_URL),
    GOLD("贵金属", ApiConfig.GOLD_BASE_URL)
}
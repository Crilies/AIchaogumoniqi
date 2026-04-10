package com.example.aichaogumoniqi.util

import android.content.Context
import android.os.Environment
import com.example.aichaogumoniqi.data.model.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object CsvUtils {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val fileNameFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    
    /**
     * 导出股票数据到CSV文件
     */
    fun exportStockData(
        context: Context,
        stockCode: String,
        transactions: List<Transaction>,
        stockHistory: List<Stock>
    ): File? {
        return try {
            val timestamp = fileNameFormat.format(Date())
            val fileName = "stock_${stockCode}_$timestamp.csv"
            val file = File(getExportDirectory(context), fileName)
            
            FileWriter(file).use { writer ->
                // 写入股票基本信息
                writer.append("股票代码,股票名称\n")
                writer.append("$stockCode,${stockHistory.firstOrNull()?.name ?: "未知"}\n\n")
                
                // 写入历史行情
                writer.append("=== 历史行情 ===\n")
                writer.append("时间,开盘价,最高价,最低价,收盘价,成交量,成交额,涨跌幅\n")
                
                stockHistory.forEach { stock ->
                    writer.append("${dateFormat.format(Date(stock.timestamp))},")
                    writer.append("${stock.open},")
                    writer.append("${stock.high},")
                    writer.append("${stock.low},")
                    writer.append("${stock.close},")
                    writer.append("${stock.volume},")
                    writer.append("${stock.amount},")
                    writer.append("${stock.changePercent}%\n")
                }
                
                writer.append("\n")
                
                // 写入交易记录
                writer.append("=== 交易记录 ===\n")
                writer.append("时间,类型,数量,价格,金额,手续费,状态\n")
                
                transactions.forEach { transaction ->
                    writer.append("${dateFormat.format(Date(transaction.timestamp))},")
                    writer.append("${if (transaction.type == "buy") "买入" else "卖出"},")
                    writer.append("${transaction.quantity},")
                    writer.append("${transaction.price},")
                    writer.append("${transaction.amount},")
                    writer.append("${transaction.fee},")
                    writer.append("${transaction.status}\n")
                }
            }
            
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 导出持仓数据到CSV文件
     */
    fun exportPortfolioData(
        context: Context,
        purchasedStocks: List<PurchasedStock>,
        watchedStocks: List<WatchedStock>
    ): File? {
        return try {
            val timestamp = fileNameFormat.format(Date())
            val fileName = "portfolio_$timestamp.csv"
            val file = File(getExportDirectory(context), fileName)
            
            FileWriter(file).use { writer ->
                // 写入持仓股票
                writer.append("=== 持仓股票 ===\n")
                writer.append("代码,名称,持仓数量,成本价,当前价,盈亏金额,盈亏比例\n")
                
                purchasedStocks.forEach { stock ->
                    writer.append("${stock.code},")
                    writer.append("${stock.name},")
                    writer.append("${stock.quantity},")
                    writer.append("${stock.costPrice},")
                    writer.append("${stock.currentPrice},")
                    writer.append("${stock.profitLoss},")
                    writer.append("${String.format("%.2f", stock.profitLossPercent)}%\n")
                }
                
                writer.append("\n")
                
                // 写入关注股票
                writer.append("=== 关注股票 ===\n")
                writer.append("代码,名称,当前价,涨跌幅,添加时间\n")
                
                watchedStocks.forEach { stock ->
                    writer.append("${stock.code},")
                    writer.append("${stock.name},")
                    writer.append("${stock.price},")
                    writer.append("${stock.change}%,")
                    writer.append("${dateFormat.format(Date(stock.addedTime))}\n")
                }
            }
            
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 导出账户数据到CSV文件
     */
    fun exportAccountData(
        context: Context,
        account: Account,
        transactions: List<Transaction>
    ): File? {
        return try {
            val timestamp = fileNameFormat.format(Date())
            val fileName = "account_$timestamp.csv"
            val file = File(getExportDirectory(context), fileName)
            
            FileWriter(file).use { writer ->
                // 写入账户信息
                writer.append("=== 账户信息 ===\n")
                writer.append("总资产,${account.totalAssets}\n")
                writer.append("可用资金,${account.availableCash}\n")
                writer.append("持仓市值,${account.marketValue}\n")
                writer.append("今日盈亏,${account.todayProfitLoss}\n")
                writer.append("累计盈亏,${account.totalProfitLoss}\n")
                writer.append("累计收益率,${String.format("%.2f", account.profitRate)}%\n")
                writer.append("初始资金,${account.initialCapital}\n")
                writer.append("币种,${account.currency}\n\n")
                
                // 写入交易统计
                val totalTransactions = transactions.size
                val buyTransactions = transactions.count { it.type == "buy" }
                val sellTransactions = transactions.count { it.type == "sell" }
                val profitableTransactions = transactions.count { it.type == "sell" && it.amount > 0 }
                
                writer.append("=== 交易统计 ===\n")
                writer.append("总交易次数,$totalTransactions\n")
                writer.append("买入次数,$buyTransactions\n")
                writer.append("卖出次数,$sellTransactions\n")
                writer.append("盈利交易,$profitableTransactions\n")
                writer.append("胜率,${if (sellTransactions > 0) String.format("%.1f", profitableTransactions.toDouble() / sellTransactions * 100) else 0}%\n\n")
                
                // 写入所有交易记录
                writer.append("=== 完整交易记录 ===\n")
                writer.append("时间,股票代码,股票名称,类型,数量,价格,金额,手续费,状态\n")
                
                transactions.forEach { transaction ->
                    writer.append("${dateFormat.format(Date(transaction.timestamp))},")
                    writer.append("${transaction.code},")
                    writer.append("${transaction.name},")
                    writer.append("${if (transaction.type == "buy") "买入" else "卖出"},")
                    writer.append("${transaction.quantity},")
                    writer.append("${transaction.price},")
                    writer.append("${transaction.amount},")
                    writer.append("${transaction.fee},")
                    writer.append("${transaction.status}\n")
                }
            }
            
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 从CSV文件导入数据
     */
    fun importFromCsv(context: Context, file: File): List<String> {
        val lines = mutableListOf<String>()
        
        try {
            FileReader(file).use { reader ->
                BufferedReader(reader).use { bufferedReader ->
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        line?.let { lines.add(it) }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return lines
    }
    
    /**
     * 获取导出目录
     */
    private fun getExportDirectory(context: Context): File {
        val exportDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "StockSimulator"
        )
        
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        
        return exportDir
    }
    
    /**
     * 获取所有导出的文件
     */
    fun getExportedFiles(context: Context): List<File> {
        val exportDir = getExportDirectory(context)
        return exportDir.listFiles()?.filter { it.extension == "csv" }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }
    
    /**
     * 删除导出的文件
     */
    fun deleteExportedFile(file: File): Boolean {
        return try {
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
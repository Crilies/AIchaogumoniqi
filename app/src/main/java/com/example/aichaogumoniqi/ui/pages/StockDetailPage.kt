package com.example.aichaogumoniqi.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aichaogumoniqi.ui.components.StockTrendChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailPage(
    stockCode: String,
    stockName: String,
    onBack: () -> Unit,
    onAddToWatchlist: () -> Unit = {}
) {
    var selectedPeriod by remember { mutableStateOf("1d") }
    val periods = listOf("1d" to "日", "1w" to "周", "1m" to "月", "6m" to "半年", "1y" to "年")
    
    // 模拟股票数据
    val stockPrice = 12.34
    val stockChange = 2.5
    val stockOpen = 12.30
    val stockHigh = 12.40
    val stockLow = 12.25
    val stockVolume = 1000000L
    val stockAmount = 12340000.0
    
    // 模拟趋势数据
    val trendData = remember(selectedPeriod) {
        listOf(12.34, 12.36, 12.35, 12.38, 12.40, 12.39, 12.42, 12.41, 12.43, 12.45)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$stockName ($stockCode)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = onAddToWatchlist) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "添加关注")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 价格信息
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "¥$stockPrice",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = "${if (stockChange >= 0) "+" else ""}$stockChange%",
                                style = MaterialTheme.typography.headlineSmall,
                                color = if (stockChange >= 0) 
                                    MaterialTheme.colorScheme.error 
                                else 
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text("今开: ¥$stockOpen")
                            Text("最高: ¥$stockHigh")
                            Text("最低: ¥$stockLow")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("成交量")
                            Text("${stockVolume / 10000}万手")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("成交额")
                            Text("${stockAmount / 100000000}亿")
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("换手率")
                            Text("1.2%")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 图表选择
            Text(
                text = "走势图",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // 时间范围选择
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                periods.forEach { (key, label) ->
                    FilterChip(
                        selected = selectedPeriod == key,
                        onClick = { selectedPeriod = key },
                        label = { Text(label) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 图表
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                StockTrendChart(
                    stockData = trendData,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 交易按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* 买入 */ },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("买入")
                }
                
                Button(
                    onClick = { /* 卖出 */ },
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("卖出")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 技术指标
            Text(
                text = "技术指标",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("MA5")
                            Text("12.35")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("MA10")
                            Text("12.32")
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("MA20")
                            Text("12.28")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("MACD")
                            Text("0.15")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("KDJ")
                            Text("65.2")
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("RSI")
                            Text("58.7")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 公司信息
            Text(
                text = "公司信息",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("公司名称: 平安银行股份有限公司")
                    Text("所属行业: 银行")
                    Text("上市日期: 1991-04-03")
                    Text("总市值: 2,389.45亿")
                    Text("流通市值: 2,389.45亿")
                }
            }
        }
    }
}
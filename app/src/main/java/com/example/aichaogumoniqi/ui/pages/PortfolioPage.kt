package com.example.aichaogumoniqi.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioPage(
    modifier: Modifier = Modifier,
    onStockClick: (code: String, name: String) -> Unit = { _, _ -> }
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("已购入", "关注")
    
    // 模拟数据
    val purchasedStocks = remember { mutableStateListOf(
        PurchasedStock("平安银行", "000001", 1000, 12.34, 12.50),
        PurchasedStock("贵州茅台", "600519", 100, 1800.00, 1856.00),
        PurchasedStock("宁德时代", "300750", 200, 210.00, 215.50)
    )}
    
    val watchedStocks = remember { mutableStateListOf(
        WatchedStock("比亚迪", "002594", 265.80, -0.5),
        WatchedStock("中国平安", "601318", 48.76, 0.8),
        WatchedStock("招商银行", "600036", 32.45, 1.5),
        WatchedStock("五粮液", "000858", 165.30, -2.1)
    )}
    
    Column(modifier = modifier.fillMaxSize()) {
        // 顶部标签栏
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        // 内容区域
        when (selectedTab) {
            0 -> PurchasedStocksContent(
                stocks = purchasedStocks,
                onStockClick = onStockClick
            )
            1 -> WatchedStocksContent(
                stocks = watchedStocks,
                onStockClick = onStockClick
            )
        }
    }
}

@Composable
fun PurchasedStocksContent(
    stocks: List<PurchasedStock>,
    onStockClick: (code: String, name: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 搜索框
        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("搜索已购入股票") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // 总资产概览
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "持仓总览",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("总资产")
                        Text(
                            text = "¥125,680.00",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("今日盈亏")
                        Text(
                            text = "+¥2,345.67",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
        
        // 持仓列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stocks) { stock ->
                PurchasedStockCard(
                    stock = stock,
                    onClick = { onStockClick(stock.code, stock.name) }
                )
            }
        }
    }
}

@Composable
fun WatchedStocksContent(
    stocks: List<WatchedStock>,
    onStockClick: (code: String, name: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 添加按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "关注列表",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { /* 添加关注 */ }) {
                Icon(Icons.Default.Add, contentDescription = "添加关注")
            }
        }
        
        // 搜索框
        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("搜索股票添加关注") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // 关注列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stocks) { stock ->
                WatchedStockCard(
                    stock = stock,
                    onClick = { onStockClick(stock.code, stock.name) }
                )
            }
        }
    }
}

@Composable
fun PurchasedStockCard(
    stock: PurchasedStock,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        text = stock.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stock.code,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "¥${stock.currentPrice}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    val profit = (stock.currentPrice - stock.costPrice) * stock.quantity
                    val profitPercent = ((stock.currentPrice - stock.costPrice) / stock.costPrice) * 100
                    Text(
                        text = "${if (profit >= 0) "+" else ""}¥${String.format("%.2f", profit)} (${if (profitPercent >= 0) "+" else ""}${String.format("%.2f", profitPercent)}%)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (profit >= 0) 
                            MaterialTheme.colorScheme.error 
                        else 
                            MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("持仓: ${stock.quantity}股")
                Text("成本: ¥${stock.costPrice}")
            }
        }
    }
}

@Composable
fun WatchedStockCard(
    stock: WatchedStock,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stock.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stock.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "¥${stock.price}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${if (stock.change >= 0) "+" else ""}${stock.change}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (stock.change >= 0) 
                        MaterialTheme.colorScheme.error 
                    else 
                        MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

data class PurchasedStock(
    val name: String,
    val code: String,
    val quantity: Int,
    val costPrice: Double,
    val currentPrice: Double
)

data class WatchedStock(
    val name: String,
    val code: String,
    val price: Double,
    val change: Double
)
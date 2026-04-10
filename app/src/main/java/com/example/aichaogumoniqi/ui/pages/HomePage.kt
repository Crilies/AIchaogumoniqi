package com.example.aichaogumoniqi.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onStockClick: (code: String, name: String) -> Unit = { _, _ -> }
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val recommendedStocks = remember { mutableStateListOf<StockRecommendation>() }
    
    // 模拟推荐股票数据
    LaunchedEffect(Unit) {
        // 这里应该从API获取数据
        recommendedStocks.addAll(
            listOf(
                StockRecommendation("平安银行", "000001", 12.34, 2.5),
                StockRecommendation("万科A", "000002", 18.56, -1.2),
                StockRecommendation("贵州茅台", "600519", 1856.00, 1.8),
                StockRecommendation("宁德时代", "300750", 215.50, 3.2),
                StockRecommendation("比亚迪", "002594", 265.80, -0.5),
                StockRecommendation("中国平安", "601318", 48.76, 0.8),
                StockRecommendation("招商银行", "600036", 32.45, 1.5),
                StockRecommendation("五粮液", "000858", 165.30, -2.1),
                StockRecommendation("隆基绿能", "601012", 25.80, 4.2),
                StockRecommendation("中信证券", "600030", 22.15, 0.3)
            )
        )
    }
    
    // 下拉刷新逻辑
    val pullRefreshState = rememberPullToRefreshState()
    
    Box(modifier = modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                // 模拟刷新操作
                recommendedStocks.clear()
                // 重新加载数据
                isRefreshing = false
            },
            state = pullRefreshState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "AI智能推荐",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "根据您的风险偏好和市场趋势为您推荐",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recommendedStocks) { stock ->
                        StockRecommendationCard(
                            stock = stock,
                            onClick = { onStockClick(stock.code, stock.name) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StockRecommendationCard(
    stock: StockRecommendation,
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
}

data class StockRecommendation(
    val name: String,
    val code: String,
    val price: Double,
    val change: Double
)
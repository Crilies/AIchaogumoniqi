package com.example.aichaogumoniqi.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aichaogumoniqi.ui.components.AssetChart
import com.example.aichaogumoniqi.ui.components.ProfitLossChart
import com.example.aichaogumoniqi.ui.components.HoldingsPieChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {}
) {
    var showSettings by remember { mutableStateOf(false) }
    
    if (showSettings) {
        SettingsPage(onBack = { showSettings = false })
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("个人中心") },
                    actions = {
                        IconButton(onClick = { showSettings = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "设置")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // 账户概览
                AccountOverviewCard()
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 收益图表
                Text(
                    text = "收益走势",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                AssetChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 盈亏分布
                Text(
                    text = "盈亏分布",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfitLossChart(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    )
                    
                    HoldingsPieChart(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 交易统计
                Text(
                    text = "交易统计",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                TransactionStatsCard()
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 快速操作
                QuickActionsCard()
            }
        }
    }
}

@Composable
fun AccountOverviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "账户概览",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("总资产")
                    Text(
                        text = "¥125,680.00",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("累计收益")
                    Text(
                        text = "+¥5,680.00",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("可用资金")
                    Text("¥25,000.00")
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("持仓市值")
                    Text("¥100,680.00")
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("今日盈亏")
                    Text(
                        text = "+¥2,345.67",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionStatsCard() {
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
                    Text("总交易次数")
                    Text(
                        text = "42",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("盈利次数")
                    Text(
                        text = "28",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("胜率")
                    Text(
                        text = "66.7%",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("最大盈利")
                    Text(
                        text = "+¥3,200.00",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("最大亏损")
                    Text(
                        text = "-¥800.00",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("平均持仓天数")
                    Text("12天")
                }
            }
        }
    }
}

@Composable
fun QuickActionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "快速操作",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* 导出数据 */ }) {
                    Text("导出数据")
                }
                
                Button(onClick = { /* 导入数据 */ }) {
                    Text("导入数据")
                }
                
                OutlinedButton(onClick = { /* 重置账户 */ }) {
                    Text("重置账户")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
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
            // 字体大小设置
            FontSizeSettings()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 深色模式设置
            DarkModeSettings()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 数据源设置
            DataSourceSettings()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 风险偏好设置
            RiskPreferenceSettings()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 交易设置
            TradingSettings()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // MCP服务设置
            MCPSettings()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 关于
            AboutSection()
        }
    }
}

@Composable
fun FontSizeSettings() {
    var fontSize by remember { mutableStateOf(16f) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "字体大小",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text("当前字体大小: ${fontSize.toInt()}sp")
            
            Slider(
                value = fontSize,
                onValueChange = { fontSize = it },
                valueRange = 12f..24f,
                steps = 11
            )
        }
    }
}

@Composable
fun DarkModeSettings() {
    var darkMode by remember { mutableStateOf(0) } // 0: 跟随系统, 1: 浅色, 2: 深色
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "深色模式",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = darkMode == 0,
                    onClick = { darkMode = 0 },
                    label = { Text("跟随系统") }
                )
                
                FilterChip(
                    selected = darkMode == 1,
                    onClick = { darkMode = 1 },
                    label = { Text("浅色") }
                )
                
                FilterChip(
                    selected = darkMode == 2,
                    onClick = { darkMode = 2 },
                    label = { Text("深色") }
                )
            }
        }
    }
}

@Composable
fun DataSourceSettings() {
    var selectedSource by remember { mutableStateOf(0) }
    val dataSources = listOf("新浪财经", "腾讯股票", "东方财富", "同花顺")
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "数据源设置",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            dataSources.forEachIndexed { index, source ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedSource == index,
                        onClick = { selectedSource = index }
                    )
                    Text(
                        text = source,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RiskPreferenceSettings() {
    var riskLevel by remember { mutableStateOf(1) } // 0: 低风险, 1: 中风险, 2: 高风险
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "风险偏好",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text("用于AI推荐股票时的风险筛选")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = riskLevel == 0,
                    onClick = { riskLevel = 0 },
                    label = { Text("低风险") }
                )
                
                FilterChip(
                    selected = riskLevel == 1,
                    onClick = { riskLevel = 1 },
                    label = { Text("中风险") }
                )
                
                FilterChip(
                    selected = riskLevel == 2,
                    onClick = { riskLevel = 2 },
                    label = { Text("高风险") }
                )
            }
        }
    }
}

@Composable
fun TradingSettings() {
    var initialCapital by remember { mutableStateOf("1000000") }
    var commissionRate by remember { mutableStateOf("0.0003") }
    var stampTaxRate by remember { mutableStateOf("0.001") }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "交易设置",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = initialCapital,
                onValueChange = { initialCapital = it },
                label = { Text("初始虚拟资金") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            
            OutlinedTextField(
                value = commissionRate,
                onValueChange = { commissionRate = it },
                label = { Text("手续费率") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            
            OutlinedTextField(
                value = stampTaxRate,
                onValueChange = { stampTaxRate = it },
                label = { Text("印花税率") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun MCPSettings() {
    var mcpEnabled by remember { mutableStateOf(false) }
    var mcpPort by remember { mutableStateOf("8080") }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                        text = "MCP服务",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "允许外部AI控制交易",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Switch(
                    checked = mcpEnabled,
                    onCheckedChange = { mcpEnabled = it }
                )
            }
            
            if (mcpEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = mcpPort,
                    onValueChange = { mcpPort = it },
                    label = { Text("服务端口") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "状态: 运行中",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "接口地址: http://localhost:$mcpPort/mcp",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Button(
                    onClick = { /* 查看接口文档 */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("查看接口文档")
                }
            }
        }
    }
}

@Composable
fun AboutSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "关于",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text("AI炒股模拟器 v1.0.0")
            Text("基于AI的智能投资模拟应用")
            Text("仅供学习交流使用，不构成投资建议")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "更新日志",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text("• 支持多数据源切换")
            Text("• 新增小米超级岛功能")
            Text("• 优化图表显示效果")
            Text("• 修复已知问题")
        }
    }
}
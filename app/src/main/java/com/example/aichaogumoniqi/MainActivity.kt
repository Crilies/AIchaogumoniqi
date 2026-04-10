package com.example.aichaogumoniqi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.aichaogumoniqi.service.MCPServer
import com.example.aichaogumoniqi.service.SuperIslandService
import com.example.aichaogumoniqi.ui.pages.HomePage
import com.example.aichaogumoniqi.ui.pages.PortfolioPage
import com.example.aichaogumoniqi.ui.pages.ProfilePage
import com.example.aichaogumoniqi.ui.pages.StockDetailPage
import com.example.aichaogumoniqi.ui.theme.AIchaogumoniqiTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // 启动服务
        startServices()
        
        setContent {
            AIchaogumoniqiTheme {
                AIchaogumoniqiApp()
            }
        }
    }
    
    private fun startServices() {
        // 启动超级岛服务
        SuperIslandService.startService(this)
        
        // 启动MCP服务
        MCPServer.startService(this)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // 停止服务
        SuperIslandService.stopService(this)
        MCPServer.stopService(this)
    }
}

@PreviewScreenSizes
@Composable
fun AIchaogumoniqiApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var showStockDetail by rememberSaveable { mutableStateOf(false) }
    var selectedStockCode by rememberSaveable { mutableStateOf("") }
    var selectedStockName by rememberSaveable { mutableStateOf("") }
    
    if (showStockDetail) {
        StockDetailPage(
            stockCode = selectedStockCode,
            stockName = selectedStockName,
            onBack = { showStockDetail = false }
        )
    } else {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                painterResource(it.icon),
                                contentDescription = it.label
                            )
                        },
                        label = { Text(it.label) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it }
                    )
                }
            }
        ) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                when (currentDestination) {
                    AppDestinations.HOME -> HomePage(
                        modifier = Modifier.padding(innerPadding),
                        onStockClick = { code, name ->
                            selectedStockCode = code
                            selectedStockName = name
                            showStockDetail = true
                        }
                    )
                    AppDestinations.PORTFOLIO -> PortfolioPage(
                        modifier = Modifier.padding(innerPadding),
                        onStockClick = { code, name ->
                            selectedStockCode = code
                            selectedStockName = name
                            showStockDetail = true
                        }
                    )
                    AppDestinations.PROFILE -> ProfilePage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("推荐", R.drawable.ic_home),
    PORTFOLIO("持仓", R.drawable.ic_favorite),
    PROFILE("我的", R.drawable.ic_account_box),
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AIchaogumoniqiTheme {
        AIchaogumoniqiApp()
    }
}
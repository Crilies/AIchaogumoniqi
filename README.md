# AI炒股模拟器 Android应用

## 项目简介
这是一个基于AI的炒股模拟器Android应用，支持实时股票数据、模拟交易、小米超级岛显示、MCP服务接口等功能。

## 主要功能
1. **首页推荐**：根据风险偏好推荐股票（高/中/低风险）
2. **持仓管理**：查看已购入股票和关注股票
3. **个人中心**：查看资产数据、图表分析、设置等
4. **小米超级岛**：实时显示总盈亏，每2秒更新
5. **MCP服务**：提供JSON-RPC接口，允许外部AI控制交易

## 技术栈
- **语言**：Kotlin
- **UI框架**：Jetpack Compose
- **架构**：MVVM + Clean Architecture
- **依赖注入**：Hilt
- **网络请求**：Retrofit + OkHttp
- **本地存储**：Room数据库
- **异步处理**：Kotlin Coroutines + Flow
- **图表**：自定义Canvas绘制

## 项目结构
```
app/
├── src/main/
│   ├── java/com/example/aichaogumoniqi/
│   │   ├── MainActivity.kt
│   │   ├── StockApplication.kt
│   │   ├── ui/
│   │   │   ├── pages/
│   │   │   │   ├── HomePage.kt
│   │   │   │   ├── PortfolioPage.kt
│   │   │   │   ├── ProfilePage.kt
│   │   │   │   ├── SettingsPage.kt
│   │   │   │   ├── StockDetailPage.kt
│   │   │   ├── components/
│   │   │   │   ├── Charts.kt
│   │   │   ├── viewmodel/
│   │   │   │   ├── StockViewModel.kt
│   │   │   ├── theme/
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── Stock.kt
│   │   │   ├── repository/
│   │   │   │   ├── StockRepository.kt
│   │   │   ├── api/
│   │   │   │   ├── StockApiService.kt
│   │   │   ├── local/
│   │   │   │   ├── AppDatabase.kt
│   │   ├── service/
│   │   │   ├── MCPServer.kt
│   │   │   ├── SuperIslandService.kt
│   │   │   ├── BootReceiver.kt
│   │   ├── di/
│   │   │   ├── AppModule.kt
│   │   ├── util/
│   │   │   ├── NetworkUtils.kt
│   │   │   ├── CsvUtils.kt
│   ├── res/
│   ├── AndroidManifest.xml
```

## 导入Android Studio步骤

### 1. 打开Android Studio
- 启动Android Studio
- 选择"Open an Existing Project"

### 2. 选择项目路径
- 导航到：`/sdcard/Documents/Xiaomi-miclaw/程序/AIchaogumoniqi`
- 选择整个项目文件夹
- 点击"OK"

### 3. 等待同步
- Android Studio会自动同步Gradle
- 等待依赖下载完成
- 如果有错误，点击"Try Again"或"Sync Now"

### 4. 配置SDK
- 确保已安装Android SDK 36
- 如果没有，通过SDK Manager安装

### 5. 运行应用
- 连接Android设备或启动模拟器
- 点击"Run"按钮（绿色三角形）
- 选择目标设备
- 等待应用安装和启动

## MCP服务使用说明

### 启动MCP服务
应用启动后会自动启动MCP服务，默认端口8080。

### 接口地址
```
http://localhost:8080/mcp
```

### 支持的方法
1. `searchStock` - 搜索股票
2. `getStockTrend` - 获取股票趋势
3. `getPortfolio` - 获取持仓信息
4. `buyStock` - 买入股票
5. `sellStock` - 卖出股票
6. `cancelOrder` - 撤单
7. `getAccountInfo` - 获取账户信息
8. `getTransactionHistory` - 获取交易历史
9. `getRecommendations` - 获取推荐股票
10. `getWatchlist` - 获取关注列表
11. `addToWatchlist` - 添加关注
12. `removeFromWatchlist` - 移除关注
13. `getMarketStatus` - 获取市场状态
14. `getStockPrice` - 获取股票价格
15. `getAccountBalance` - 获取账户余额

### 示例请求
```json
{
  "jsonrpc": "2.0",
  "method": "buyStock",
  "params": {
    "code": "000001",
    "quantity": 100,
    "price": 12.34,
    "orderType": "limit"
  },
  "id": 1
}
```

### 示例响应
```json
{
  "jsonrpc": "2.0",
  "result": {
    "orderId": "BUY_1234567890",
    "code": "000001",
    "quantity": 100,
    "price": 12.34,
    "type": "limit",
    "status": "submitted",
    "timestamp": 1234567890
  },
  "id": 1
}
```

## 小米超级岛功能

### 功能说明
- 应用在后台时自动显示超级岛
- 每2秒更新一次总盈亏
- 收起状态：应用图标 + 红绿涨跌箭头 + 数值
- 展开状态：一小时总涨跌折线图

### 权限要求
- 自启动权限
- 前台服务权限
- 通知权限

### 离线处理
- 网络断开时显示"网络错误"提示
- 3秒后自动关闭超级岛

## 数据导出功能

### 导出格式
CSV格式，支持导出：
- 股票数据（历史行情 + 交易记录）
- 持仓数据（持仓股票 + 关注股票）
- 账户数据（账户信息 + 完整交易记录）

### 导出位置
`/sdcard/Documents/StockSimulator/`

## 设置选项

### 1. 字体大小
- 可调节范围：12sp - 24sp

### 2. 深色模式
- 跟随系统
- 浅色模式
- 深色模式

### 3. 数据源设置
- 新浪财经
- 腾讯股票
- 东方财富
- 同花顺

### 4. 风险偏好
- 低风险
- 中风险
- 高风险

### 5. 交易设置
- 初始虚拟资金（默认100万）
- 手续费率（默认0.03%）
- 印花税率（默认0.1%）

### 6. MCP服务
- 启用/禁用
- 端口设置（默认8080）
- 接口文档查看

## 开发注意事项

### 1. 代码结构
- 遵循MVVM架构
- 使用Hilt进行依赖注入
- 使用Room进行本地存储
- 使用Retrofit进行网络请求

### 2. 性能优化
- 图表使用Canvas绘制，避免过度绘制
- 使用协程处理异步操作
- 实现数据缓存机制

### 3. 网络处理
- 实现请求重试机制
- 添加网络状态监听
- 处理离线状态

### 4. 错误处理
- 统一错误处理机制
- 友好的错误提示
- 异常日志记录

## 版本信息
- 版本号：1.0.0
- 最低支持：Android 10 (API 24)
- 目标版本：Android 16 (API 36)
- 编译版本：Android 16 (API 36)

## 许可证
仅供学习交流使用，不构成投资建议。

## 联系方式
如有问题，请通过GitHub Issues反馈。
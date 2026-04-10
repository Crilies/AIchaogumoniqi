# 小米超级岛配置文档

## 概述
本应用集成小米超级岛（焦点通知）功能，用于实时显示股票盈亏信息。

## 功能特性
- **实时更新**：每2秒更新一次总盈亏数据
- **后台运行**：应用在后台时自动显示超级岛
- **离线处理**：网络断开时显示错误提示并关闭超级岛
- **交互支持**：支持点击展开查看详情

## 显示内容

### 收起状态（摘要态）
- **左侧**：应用图标
- **中间**：红绿涨跌箭头
- **右侧**：盈亏数值

### 展开状态（焦点通知）
- **标题**：实时盈亏
- **内容**：盈亏数值
- **图表**：一小时总涨跌折线图

## 技术实现

### 1. 通知渠道配置
```kotlin
private const val CHANNEL_ID = "stock_super_island"

val channel = NotificationChannel(
    CHANNEL_ID,
    "股票超级岛",
    NotificationManager.IMPORTANCE_HIGH
).apply {
    description = "显示实时股票盈亏信息"
    setShowBadge(true)
    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
}
```

### 2. 扩展参数配置
```kotlin
val focusParam = JSONObject().apply {
    // 摘要态
    put("summary", JSONObject().apply {
        put("title", "AI炒股模拟器")
        put("content", "实时盈亏: $profitText")
        put("icon", R.mipmap.ic_launcher)
    })
    
    // 焦点通知
    put("focus", JSONObject().apply {
        put("title", "实时盈亏")
        put("content", profitText)
        put("color", if (isProfit) "#FF0000" else "#00FF00")
        put("ticker", profitText)
        put("tickerPic", if (isProfit) "arrow_up" else "arrow_down")
    })
    
    // 交互能力
    put("interaction", JSONObject().apply {
        put("click", JSONObject().apply {
            put("action", "open_app")
        })
        put("longPress", JSONObject().apply {
            put("action", "stop_service")
        })
    })
    
    // 息屏显示
    put("aodTitle", "AI炒股")
    put("aodPic", R.mipmap.ic_launcher)
    
    // 状态栏显示
    put("ticker", profitText)
    put("tickerPic", if (isProfit) "arrow_up" else "arrow_down")
}
```

### 3. 通知构建
```kotlin
NotificationCompat.Builder(context, CHANNEL_ID)
    .setContentTitle("实时盈亏")
    .setContentText(profitText)
    .setSmallIcon(R.mipmap.ic_launcher)
    .setContentIntent(pendingIntent)
    .setOngoing(true)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setCategory(NotificationCompat.CATEGORY_SERVICE)
    .setExtras(android.os.Bundle().apply {
        putString("miui.focus.param", focusParam.toString())
    })
    .build()
```

## 权限要求

### 1. 必需权限
```xml
<!-- 前台服务权限 -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />

<!-- 通知权限 -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- 自启动权限 -->
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

### 2. 可选权限
```xml
<!-- 悬浮窗权限（用于超级岛显示） -->
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

<!-- 保持后台运行 -->
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

## 服务配置

### 1. 服务声明
```xml
<service
    android:name=".service.SuperIslandService"
    android:enabled="true"
    android:exported="false"
    android:foregroundServiceType="dataSync" />
```

### 2. 自启动接收器
```xml
<receiver
    android:name=".service.BootReceiver"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
        <action android:name="android.intent.action.QUICKBOOT_POWERON" />
    </intent-filter>
</receiver>
```

### 3. 超级岛支持配置
```xml
<meta-data
    android:name="miui.focus.support"
    android:value="true" />
```

## 运行机制

### 1. 启动流程
1. 应用启动时自动启动SuperIslandService
2. 服务创建通知渠道
3. 服务启动前台通知
4. 服务启动定时更新任务（每2秒）

### 2. 更新流程
1. 定时器每2秒触发一次
2. 获取最新盈亏数据
3. 更新通知内容
4. 更新小米超级岛扩展参数

### 3. 停止流程
1. 应用销毁时停止服务
2. 网络错误时显示错误通知
3. 错误通知3秒后自动关闭

## 离线处理

### 1. 网络状态检测
```kotlin
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
               capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
               capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo
        @Suppress("DEPRECATION")
        return networkInfo?.isConnected == true
    }
}
```

### 2. 离线处理逻辑
```kotlin
private suspend fun updateSuperIsland() {
    if (isNetworkAvailable) {
        // 更新正常数据
        updateNotificationWithProfitData()
    } else {
        // 显示网络错误
        showNetworkErrorNotification()
        // 3秒后关闭服务
        delay(3000)
        stopSelf()
    }
}
```

## 图片资源规范

### 1. 图片要求
- **大小限制**：单张图片 ≤ 100KB
- **格式要求**：HTTPS链接
- **宽高比**：1:1 到 16:9 (1.78)
- **数量限制**：单条通知最多10张

### 2. 应用图标
- 使用应用默认图标
- 确保图标清晰可辨

## 兼容性检查

### 1. 设备支持检查
```kotlin
fun isSuperIslandSupported(): Boolean {
    return try {
        // 检查小米系统版本
        Build.MANUFACTURER.equals("Xiaomi", ignoreCase = true) &&
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    } catch (e: Exception) {
        false
    }
}
```

### 2. 权限检查
```kotlin
fun hasFocusNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}
```

## 交互功能

### 1. 点击交互
- **点击通知**：打开应用主界面
- **长按通知**：停止超级岛服务

### 2. 展开交互
- **下拉展开**：查看详细图表
- **拖拽分享**：支持分享盈亏信息

## 性能优化

### 1. 更新频率
- 默认每2秒更新一次
- 可根据需要调整更新频率

### 2. 内存管理
- 及时释放不需要的资源
- 避免内存泄漏

### 3. 电量优化
- 使用前台服务保证运行
- 合理控制更新频率

## 故障排除

### 1. 超级岛不显示
**可能原因**：
- 未授予通知权限
- 未授予自启动权限
- 设备不支持超级岛

**解决方法**：
- 检查并授予通知权限
- 检查并授予自启动权限
- 确认设备支持小米超级岛

### 2. 更新不及时
**可能原因**：
- 网络连接问题
- 服务被系统杀死
- 应用被强制停止

**解决方法**：
- 检查网络连接
- 重新启动应用
- 设置应用白名单

### 3. 离线处理异常
**可能原因**：
- 网络状态检测不准确
- 错误处理逻辑问题

**解决方法**：
- 检查网络权限
- 查看日志信息
- 重启应用

## 测试建议

### 1. 功能测试
- 测试正常显示
- 测试更新频率
- 测试离线处理
- 测试交互功能

### 2. 兼容性测试
- 测试不同小米设备
- 测试不同系统版本
- 测试不同网络环境

### 3. 性能测试
- 测试电量消耗
- 测试内存占用
- 测试长时间运行

## 更新日志

### v1.0.0
- 初始版本
- 支持基本超级岛功能
- 支持实时更新
- 支持离线处理
- 支持交互功能

## 参考文档
- [小米澎湃OS开发者平台](https://dev.mi.com/xiaomihyperos)
- [小米超级岛开发文档](https://dev.mi.com/xiaomihyperos/develop)
- [Android通知开发指南](https://developer.android.com/guide/topics/ui/notifiers/notifications)
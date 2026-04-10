# MCP服务配置文档

## 概述
本应用提供MCP（Model Context Protocol）服务接口，允许外部AI代理实时控制股票模拟交易。

## 服务配置

### 服务地址
```
默认地址: http://localhost:8080/mcp
默认端口: 8080
```

### 协议格式
使用JSON-RPC 2.0协议

### 请求格式
```json
{
  "jsonrpc": "2.0",
  "method": "方法名",
  "params": {
    "参数名": "参数值"
  },
  "id": 请求ID
}
```

### 响应格式
```json
{
  "jsonrpc": "2.0",
  "result": {
    "返回数据": "数据值"
  },
  "id": 请求ID
}
```

### 错误响应
```json
{
  "jsonrpc": "2.0",
  "error": {
    "code": 错误码,
    "message": "错误信息"
  },
  "id": 请求ID
}
```

## 支持的方法

### 1. 搜索股票
**方法名**: `searchStock`

**参数**:
- `keyword` (string): 搜索关键词

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "searchStock",
  "params": {
    "keyword": "平安"
  },
  "id": 1
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "code": "000001",
      "name": "平安银行",
      "price": 12.34,
      "change": 2.5
    }
  ],
  "id": 1
}
```

### 2. 获取股票趋势
**方法名**: `getStockTrend`

**参数**:
- `code` (string): 股票代码
- `period` (string): 时间周期 (1d, 1w, 1m, 6m, 1y)

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getStockTrend",
  "params": {
    "code": "000001",
    "period": "1d"
  },
  "id": 2
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "code": "000001",
    "period": "1d",
    "data": [12.34, 12.36, 12.35, 12.38, 12.40]
  },
  "id": 2
}
```

### 3. 获取持仓信息
**方法名**: `getPortfolio`

**参数**: 无

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getPortfolio",
  "params": {},
  "id": 3
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "code": "000001",
      "name": "平安银行",
      "quantity": 1000,
      "cost": 12.00,
      "current": 12.34
    }
  ],
  "id": 3
}
```

### 4. 买入股票
**方法名**: `buyStock`

**参数**:
- `code` (string): 股票代码
- `quantity` (int): 买入数量
- `price` (double): 买入价格
- `orderType` (string): 订单类型 (limit: 限价, market: 市价)

**示例请求**:
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
  "id": 4
}
```

**示例响应**:
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
  "id": 4
}
```

### 5. 卖出股票
**方法名**: `sellStock`

**参数**:
- `code` (string): 股票代码
- `quantity` (int): 卖出数量
- `price` (double): 卖出价格
- `orderType` (string): 订单类型 (limit: 限价, market: 市价)

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "sellStock",
  "params": {
    "code": "000001",
    "quantity": 50,
    "price": 12.50,
    "orderType": "limit"
  },
  "id": 5
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "orderId": "SELL_1234567890",
    "code": "000001",
    "quantity": 50,
    "price": 12.50,
    "type": "limit",
    "status": "submitted",
    "timestamp": 1234567890
  },
  "id": 5
}
```

### 6. 撤单
**方法名**: `cancelOrder`

**参数**:
- `orderId` (string): 订单ID

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "cancelOrder",
  "params": {
    "orderId": "BUY_1234567890"
  },
  "id": 6
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "orderId": "BUY_1234567890",
    "status": "cancelled",
    "timestamp": 1234567890
  },
  "id": 6
}
```

### 7. 获取账户信息
**方法名**: `getAccountInfo`

**参数**: 无

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getAccountInfo",
  "params": {},
  "id": 7
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "totalAssets": 125680.00,
    "availableCash": 25000.00,
    "marketValue": 100680.00,
    "todayProfitLoss": 2345.67,
    "totalProfitLoss": 5680.00,
    "profitRate": 4.5
  },
  "id": 7
}
```

### 8. 获取交易历史
**方法名**: `getTransactionHistory`

**参数**:
- `limit` (int): 返回数量限制，默认10
- `offset` (int): 偏移量，默认0

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getTransactionHistory",
  "params": {
    "limit": 10,
    "offset": 0
  },
  "id": 8
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "history": [
      {
        "id": 1,
        "type": "buy",
        "code": "000001",
        "name": "平安银行",
        "quantity": 1000,
        "price": 12.00,
        "amount": 12000.00,
        "timestamp": 1234567890
      }
    ],
    "total": 1
  },
  "id": 8
}
```

### 9. 获取推荐股票
**方法名**: `getRecommendations`

**参数**:
- `riskLevel` (string): 风险等级 (low, medium, high)
- `count` (int): 返回数量，默认10

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getRecommendations",
  "params": {
    "riskLevel": "medium",
    "count": 10
  },
  "id": 9
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "code": "000001",
      "name": "平安银行",
      "reason": "近期涨幅稳定",
      "risk": "low"
    }
  ],
  "id": 9
}
```

### 10. 获取关注列表
**方法名**: `getWatchlist`

**参数**: 无

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getWatchlist",
  "params": {},
  "id": 10
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": [
    {
      "code": "002594",
      "name": "比亚迪",
      "price": 265.80,
      "change": -0.5
    }
  ],
  "id": 10
}
```

### 11. 添加关注
**方法名**: `addToWatchlist`

**参数**:
- `code` (string): 股票代码

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "addToWatchlist",
  "params": {
    "code": "000001"
  },
  "id": 11
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "code": "000001",
    "status": "added"
  },
  "id": 11
}
```

### 12. 移除关注
**方法名**: `removeFromWatchlist`

**参数**:
- `code` (string): 股票代码

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "removeFromWatchlist",
  "params": {
    "code": "000001"
  },
  "id": 12
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "code": "000001",
    "status": "removed"
  },
  "id": 12
}
```

### 13. 获取市场状态
**方法名**: `getMarketStatus`

**参数**: 无

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getMarketStatus",
  "params": {},
  "id": 13
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "isOpen": true,
    "currentTime": 1234567890,
    "tradingHours": "9:30-11:30, 13:00-15:00",
    "timezone": "Asia/Shanghai"
  },
  "id": 13
}
```

### 14. 获取股票价格
**方法名**: `getStockPrice`

**参数**:
- `code` (string): 股票代码

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getStockPrice",
  "params": {
    "code": "000001"
  },
  "id": 14
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "code": "000001",
    "price": 12.34,
    "open": 12.30,
    "high": 12.40,
    "low": 12.25,
    "close": 12.34,
    "volume": 1000000,
    "amount": 12340000.0,
    "change": 0.04,
    "changePercent": 0.33,
    "timestamp": 1234567890
  },
  "id": 14
}
```

### 15. 获取账户余额
**方法名**: `getAccountBalance`

**参数**: 无

**示例请求**:
```json
{
  "jsonrpc": "2.0",
  "method": "getAccountBalance",
  "params": {},
  "id": 15
}
```

**示例响应**:
```json
{
  "jsonrpc": "2.0",
  "result": {
    "cash": 25000.00,
    "frozen": 0.00,
    "available": 25000.00,
    "currency": "CNY"
  },
  "id": 15
}
```

## 错误码

| 错误码 | 说明 |
|--------|------|
| -32700 | 解析错误 |
| -32600 | 无效请求 |
| -32601 | 方法不存在 |
| -32602 | 无效参数 |
| -32603 | 内部错误 |
| -32000 | 服务器错误 |
| -32001 | 股票不存在 |
| -32002 | 余额不足 |
| -32003 | 持仓不足 |
| -32004 | 交易时间未到 |
| -32005 | 订单不存在 |

## 使用示例

### Python示例
```python
import requests
import json

def call_mcp_method(method, params=None):
    url = "http://localhost:8080/mcp"
    payload = {
        "jsonrpc": "2.0",
        "method": method,
        "params": params or {},
        "id": 1
    }
    
    response = requests.post(url, json=payload)
    return response.json()

# 获取账户信息
account_info = call_mcp_method("getAccountInfo")
print(account_info)

# 买入股票
buy_result = call_mcp_method("buyStock", {
    "code": "000001",
    "quantity": 100,
    "price": 12.34,
    "orderType": "limit"
})
print(buy_result)

# 获取持仓
portfolio = call_mcp_method("getPortfolio")
print(portfolio)
```

### JavaScript示例
```javascript
async function callMcpMethod(method, params = {}) {
    const response = await fetch('http://localhost:8080/mcp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            jsonrpc: '2.0',
            method: method,
            params: params,
            id: 1
        })
    });
    
    return await response.json();
}

// 获取账户信息
callMcpMethod('getAccountInfo').then(result => {
    console.log(result);
});

// 卖出股票
callMcpMethod('sellStock', {
    code: '000001',
    quantity: 50,
    price: 12.50,
    orderType: 'limit'
}).then(result => {
    console.log(result);
});
```

### curl示例
```bash
# 获取账户信息
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "getAccountInfo",
    "params": {},
    "id": 1
  }'

# 买入股票
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "buyStock",
    "params": {
      "code": "000001",
      "quantity": 100,
      "price": 12.34,
      "orderType": "limit"
    },
    "id": 2
  }'
```

## 注意事项

1. **交易时间限制**：只能在交易时间（9:30-11:30, 13:00-15:00）进行交易
2. **涨跌停限制**：普通股票±10%，创业板/科创板±20%
3. **T+1规则**：当日买入的股票次日才能卖出
4. **手续费**：默认手续费率0.03%，印花税率0.1%（仅卖出收取）
5. **网络要求**：确保设备网络连接正常
6. **权限要求**：应用需要自启动权限以保持MCP服务运行

## 故障排除

### 1. 无法连接MCP服务
- 检查应用是否正在运行
- 检查端口是否被占用
- 检查网络连接是否正常

### 2. 交易失败
- 检查交易时间是否在交易时段内
- 检查账户余额是否充足
- 检查持仓数量是否足够

### 3. 超级岛不显示
- 检查是否授予自启动权限
- 检查是否授予通知权限
- 检查网络连接是否正常

## 更新日志

### v1.0.0
- 初始版本
- 支持基本股票交易功能
- 支持小米超级岛显示
- 支持MCP服务接口
- 支持数据导入导出
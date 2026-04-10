package com.example.aichaogumoniqi.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AssetChart(modifier: Modifier = Modifier) {
    val lineColor = MaterialTheme.colorScheme.primary
    val fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    
    // 模拟资产数据
    val dataPoints = listOf(
        100000f, 105000f, 103000f, 110000f, 108000f, 
        115000f, 112000f, 120000f, 118000f, 125000f, 125680f
    )
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 16.dp.toPx()
        
        val minValue = dataPoints.min()
        val maxValue = dataPoints.max()
        val range = maxValue - minValue
        
        val xStep = (width - 2 * padding) / (dataPoints.size - 1)
        
        // 绘制网格线
        for (i in 0..4) {
            val y = padding + (height - 2 * padding) * i / 4
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f
            )
        }
        
        // 绘制折线
        val path = Path()
        val filledPath = Path()
        
        dataPoints.forEachIndexed { index, value ->
            val x = padding + index * xStep
            val y = padding + (height - 2 * padding) * (1 - (value - minValue) / range)
            
            if (index == 0) {
                path.moveTo(x, y)
                filledPath.moveTo(x, height - padding)
                filledPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                filledPath.lineTo(x, y)
            }
        }
        
        // 完成填充路径
        filledPath.lineTo(padding + (dataPoints.size - 1) * xStep, height - padding)
        filledPath.close()
        
        // 绘制填充
        drawPath(
            path = filledPath,
            color = fillColor
        )
        
        // 绘制折线
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
fun ProfitLossChart(modifier: Modifier = Modifier) {
    val profitColor = MaterialTheme.colorScheme.error
    val lossColor = MaterialTheme.colorScheme.primary
    
    // 模拟盈亏数据
    val dataPoints = listOf(2000f, -500f, 1500f, -800f, 3000f, 1200f, -300f, 2500f)
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 16.dp.toPx()
        
        val minValue = dataPoints.min()
        val maxValue = dataPoints.max()
        val range = maxValue - minValue
        
        val xStep = (width - 2 * padding) / (dataPoints.size - 1)
        
        // 绘制零线
        val zeroY = padding + (height - 2 * padding) * (maxValue / range)
        drawLine(
            color = Color.Gray,
            start = Offset(padding, zeroY),
            end = Offset(width - padding, zeroY),
            strokeWidth = 1f
        )
        
        // 绘制柱状图
        dataPoints.forEachIndexed { index, value ->
            val x = padding + index * xStep
            val y = padding + (height - 2 * padding) * (1 - (value - minValue) / range)
            val barWidth = xStep * 0.6f
            
            val color = if (value >= 0) profitColor else lossColor
            
            drawRect(
                color = color,
                topLeft = Offset(x - barWidth / 2, minOf(y, zeroY)),
                size = androidx.compose.ui.geometry.Size(barWidth, kotlin.math.abs(y - zeroY))
            )
        }
    }
}

@Composable
fun HoldingsPieChart(modifier: Modifier = Modifier) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.inversePrimary
    )
    
    // 模拟持仓分布数据
    val data = listOf(
        "科技" to 35f,
        "消费" to 25f,
        "金融" to 20f,
        "医药" to 15f,
        "其他" to 5f
    )
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val width = size.width
        val height = size.height
        val radius = minOf(width, height) / 2 - 16.dp.toPx()
        val center = Offset(width / 2, height / 2)
        
        var startAngle = -90f
        
        data.forEachIndexed { index, (_, value) ->
            val sweepAngle = value / 100f * 360f
            
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
            )
            
            startAngle += sweepAngle
        }
    }
}

@Composable
fun StockTrendChart(
    stockData: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    val fillColor = lineColor.copy(alpha = 0.1f)
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 16.dp.toPx()
        
        val minValue = stockData.min()
        val maxValue = stockData.max()
        val range = maxValue - minValue
        
        val xStep = (width - 2 * padding) / (stockData.size - 1)
        
        val path = Path()
        val filledPath = Path()
        
        stockData.forEachIndexed { index, value ->
            val x = padding + index * xStep
            val y = padding + (height - 2 * padding) * (1 - (value - minValue) / range)
            
            if (index == 0) {
                path.moveTo(x, y)
                filledPath.moveTo(x, height - padding)
                filledPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                filledPath.lineTo(x, y)
            }
        }
        
        filledPath.lineTo(padding + (stockData.size - 1) * xStep, height - padding)
        filledPath.close()
        
        drawPath(
            path = filledPath,
            color = fillColor
        )
        
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
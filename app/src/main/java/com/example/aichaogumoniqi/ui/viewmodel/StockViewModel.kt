package com.example.aichaogumoniqi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aichaogumoniqi.data.model.*
import com.example.aichaogumoniqi.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {
    
    private val _recommendations = MutableStateFlow<List<StockRecommendation>>(emptyList())
    val recommendations: StateFlow<List<StockRecommendation>> = _recommendations.asStateFlow()
    
    private val _selectedStock = MutableStateFlow<Stock?>(null)
    val selectedStock: StateFlow<Stock?> = _selectedStock.asStateFlow()
    
    private val _stockTrend = MutableStateFlow<List<Double>>(emptyList())
    val stockTrend: StateFlow<List<Double>> = _stockTrend.asStateFlow()
    
    private val _marketStatus = MutableStateFlow<MarketStatus?>(null)
    val marketStatus: StateFlow<MarketStatus?> = _marketStatus.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadRecommendations()
        loadMarketStatus()
    }
    
    fun loadRecommendations(riskLevel: String = "medium", count: Int = 10) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val result = repository.getRecommendations(riskLevel, count)
                _recommendations.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "加载推荐股票失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadStockPrice(code: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val stock = repository.getStockPrice(code)
                _selectedStock.value = stock
            } catch (e: Exception) {
                _error.value = e.message ?: "加载股票价格失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadStockTrend(code: String, period: String = "1d") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val trend = repository.getStockTrend(code, period)
                _stockTrend.value = trend
            } catch (e: Exception) {
                _error.value = e.message ?: "加载股票趋势失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadMarketStatus() {
        viewModelScope.launch {
            try {
                val status = repository.getMarketStatus()
                _marketStatus.value = status
            } catch (e: Exception) {
                _error.value = e.message ?: "加载市场状态失败"
            }
        }
    }
    
    fun searchStocks(keyword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val results = repository.searchStocks(keyword)
                // 这里可以处理搜索结果
            } catch (e: Exception) {
                _error.value = e.message ?: "搜索股票失败"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun refreshData() {
        loadRecommendations()
        loadMarketStatus()
    }
}
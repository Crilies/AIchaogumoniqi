package com.example.aichaogumoniqi.data.local

import androidx.room.*
import com.example.aichaogumoniqi.data.model.*

@Database(
    entities = [
        PurchasedStockEntity::class,
        WatchedStockEntity::class,
        TransactionEntity::class,
        AccountEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun purchasedStockDao(): PurchasedStockDao
    abstract fun watchedStockDao(): WatchedStockDao
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    
    companion object {
        const val DATABASE_NAME = "stock_simulator_db"
    }
}

// 实体类
@Entity(tableName = "purchased_stocks")
data class PurchasedStockEntity(
    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    
    @ColumnInfo(name = "cost_price")
    val costPrice: Double,
    
    @ColumnInfo(name = "current_price")
    val currentPrice: Double,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "watched_stocks")
data class WatchedStockEntity(
    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "price")
    val price: Double,
    
    @ColumnInfo(name = "change")
    val change: Double,
    
    @ColumnInfo(name = "added_at")
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    
    @ColumnInfo(name = "type")
    val type: String, // "buy" or "sell"
    
    @ColumnInfo(name = "code")
    val code: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    
    @ColumnInfo(name = "price")
    val price: Double,
    
    @ColumnInfo(name = "amount")
    val amount: Double,
    
    @ColumnInfo(name = "fee")
    val fee: Double,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    
    @ColumnInfo(name = "status")
    val status: String = "completed",
    
    @ColumnInfo(name = "order_id")
    val orderId: String? = null
)

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1,
    
    @ColumnInfo(name = "total_assets")
    val totalAssets: Double,
    
    @ColumnInfo(name = "available_cash")
    val availableCash: Double,
    
    @ColumnInfo(name = "market_value")
    val marketValue: Double,
    
    @ColumnInfo(name = "today_profit_loss")
    val todayProfitLoss: Double,
    
    @ColumnInfo(name = "total_profit_loss")
    val totalProfitLoss: Double,
    
    @ColumnInfo(name = "initial_capital")
    val initialCapital: Double = 1000000.0,
    
    @ColumnInfo(name = "commission_rate")
    val commissionRate: Double = 0.0003,
    
    @ColumnInfo(name = "stamp_tax_rate")
    val stampTaxRate: Double = 0.001,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

// DAO接口
@Dao
interface PurchasedStockDao {
    @Query("SELECT * FROM purchased_stocks")
    suspend fun getAll(): List<PurchasedStockEntity>
    
    @Query("SELECT * FROM purchased_stocks WHERE code = :code")
    suspend fun getByCode(code: String): PurchasedStockEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: PurchasedStockEntity)
    
    @Update
    suspend fun update(stock: PurchasedStockEntity)
    
    @Delete
    suspend fun delete(stock: PurchasedStockEntity)
    
    @Query("DELETE FROM purchased_stocks WHERE code = :code")
    suspend fun deleteByCode(code: String)
    
    @Query("UPDATE purchased_stocks SET current_price = :price, updated_at = :timestamp WHERE code = :code")
    suspend fun updatePrice(code: String, price: Double, timestamp: Long = System.currentTimeMillis())
}

@Dao
interface WatchedStockDao {
    @Query("SELECT * FROM watched_stocks ORDER BY added_at DESC")
    suspend fun getAll(): List<WatchedStockEntity>
    
    @Query("SELECT * FROM watched_stocks WHERE code = :code")
    suspend fun getByCode(code: String): WatchedStockEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: WatchedStockEntity)
    
    @Delete
    suspend fun delete(stock: WatchedStockEntity)
    
    @Query("DELETE FROM watched_stocks WHERE code = :code")
    suspend fun deleteByCode(code: String)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getAll(limit: Int = 100, offset: Int = 0): List<TransactionEntity>
    
    @Query("SELECT * FROM transactions WHERE code = :code ORDER BY timestamp DESC")
    suspend fun getByCode(code: String): List<TransactionEntity>
    
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    suspend fun getByType(type: String): List<TransactionEntity>
    
    @Insert
    suspend fun insert(transaction: TransactionEntity): Long
    
    @Update
    suspend fun update(transaction: TransactionEntity)
    
    @Query("UPDATE transactions SET status = :status WHERE order_id = :orderId")
    suspend fun updateStatus(orderId: String, status: String)
    
    @Query("DELETE FROM transactions WHERE timestamp < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getCount(): Int
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM account WHERE id = 1")
    suspend fun getAccount(): AccountEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)
    
    @Update
    suspend fun update(account: AccountEntity)
    
    @Query("UPDATE account SET available_cash = :cash, updated_at = :timestamp WHERE id = 1")
    suspend fun updateCash(cash: Double, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE account SET total_assets = :totalAssets, market_value = :marketValue, updated_at = :timestamp WHERE id = 1")
    suspend fun updateAssets(totalAssets: Double, marketValue: Double, timestamp: Long = System.currentTimeMillis())
}

// 类型转换器
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}
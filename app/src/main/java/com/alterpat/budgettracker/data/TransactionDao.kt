package com.alterpat.budgettracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAll(): List<TransactionModel>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun get(id: Int): TransactionModel

    @Insert
    fun insertAll(transactionModel: TransactionModel): Long

    @Delete
    fun delete(transactionModel: TransactionModel)

    @Update
    fun update(vararg transactionModel: TransactionModel): Int

}
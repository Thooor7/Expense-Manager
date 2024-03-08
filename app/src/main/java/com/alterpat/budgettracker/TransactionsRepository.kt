package com.alterpat.budgettracker

import android.content.Context
import android.os.Bundle
import com.alterpat.budgettracker.data.AppDataBase
import com.alterpat.budgettracker.data.TransactionModel

class TransactionsRepository(
    private val context: Context) {

    private val budgetDataBaseLocal = AppDataBase.getDataBase(context).getAllTransactionDao()

    companion object {

        private lateinit var repository: TransactionsRepository
        fun getInstance(): TransactionsRepository {
            if (!::repository.isInitialized) {
                repository = TransactionsRepository(repository.context)
            }
            return repository
        }
    }


    fun getForId(id: Int): TransactionModel {
        return budgetDataBaseLocal.get(id)
    }
    fun getAll(): List<TransactionModel> {
        return budgetDataBaseLocal.getAll()
    }

    fun insert(transactionModel: TransactionModel): Boolean {
        return budgetDataBaseLocal.insertAll(transactionModel) > 0
    }
    fun delete(id: Int) {
        var get = getForId(id)
         budgetDataBaseLocal.delete(get)
    }

    fun update(transactionModel: TransactionModel): Boolean {
        val rowsUpdated = budgetDataBaseLocal.update(transactionModel)
        return rowsUpdated > 0
    }

}
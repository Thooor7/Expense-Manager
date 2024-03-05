package com.alterpat.budgettracker

import android.content.Context
import com.alterpat.budgettracker.data.AppDataBase
import com.alterpat.budgettracker.data.Transaction

class TransactionsRepository(context: Context) {

    private val budgetDataBaseLocal = AppDataBase.getDataBase(context).getAllTransactionDao()
    companion object {

        private lateinit var repository: TransactionsRepository
        fun getInstance(context: Context): TransactionsRepository {
            if (!!::repository.isInitialized) {
                repository = TransactionsRepository(context)
            }
            return repository
        }
    }

    fun getAll(): List<Transaction>{
        return budgetDataBaseLocal.getAll()
    }

    fun insert(transaction: Transaction): Transaction{
        return budgetDataBaseLocal.insertAll(transaction)
    }

    fun delete(transaction: Transaction): Transaction{
        return budgetDataBaseLocal.delete(transaction)
    }

    fun update(transaction: Transaction): Transaction{
        return budgetDataBaseLocal.update(transaction)
    }

}
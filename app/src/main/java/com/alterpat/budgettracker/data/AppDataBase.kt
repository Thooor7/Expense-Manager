package com.alterpat.budgettracker.data

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TransactionModel::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getAllTransactionDao(): TransactionDao

    companion object{
        private lateinit var INSTANCE: AppDataBase

        fun getDataBase(context: Context): AppDataBase {
            if (!::INSTANCE.isInitialized){
                synchronized(AppDataBase::class){
                    INSTANCE = Room.databaseBuilder(context, AppDataBase::class.java, "transactions")
                        .addMigrations()
                        .allowMainThreadQueries()
                        .build()
                }
            }
                return INSTANCE
        }
    }
}
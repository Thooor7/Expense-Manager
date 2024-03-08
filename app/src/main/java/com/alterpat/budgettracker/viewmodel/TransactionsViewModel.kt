package com.alterpat.budgettracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alterpat.budgettracker.TransactionsRepository
import com.alterpat.budgettracker.data.TransactionModel
import kotlinx.coroutines.launch

class TransactionsViewModel(private val repository: TransactionsRepository ) : ViewModel() {

    private val _budgetDataBaseLocal = MutableLiveData<List<TransactionModel>>()
    val budgetDataBaseLocal: LiveData<List<TransactionModel>> = _budgetDataBaseLocal


    fun getAll() {
        viewModelScope.launch {
            try {
                _budgetDataBaseLocal.value = repository.getAll()

            } catch (e: Exception){

            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                repository.delete(id)

            } catch (e: Exception) {
                //TODO Lidar com exceções
            }
        }
    }

    fun insertAll(transactionModel: TransactionModel) {
        repository.insert(transactionModel)
    }

    fun save(guest: TransactionModel) {

        viewModelScope.launch {
            try {
                if (guest.id == 0) {
                    if (repository.insert(guest)) {

                    } else {

                    }
                } else {
                    if (repository.update(guest)) {

                    } else {

                    }
                }
            } catch (e: Exception) {
                //TODO TRATAR ERRO
            }
        }
    }
}

class MainViewModelFactory(private val transactionsRepository: TransactionsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TransactionsViewModel(transactionsRepository) as T
    }


}
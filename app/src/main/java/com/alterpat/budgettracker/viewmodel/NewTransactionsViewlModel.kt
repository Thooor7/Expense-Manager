package com.alterpat.budgettracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alterpat.budgettracker.TransactionsRepository
import com.alterpat.budgettracker.data.TransactionModel
import kotlinx.coroutines.launch

class NewTransactionsViewlModel(private val repository: TransactionsRepository) : ViewModel() {


    private val _dataBaseId = MutableLiveData<TransactionModel>()
    val dataBaseId: LiveData<TransactionModel> = _dataBaseId

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _saveTransaction = MutableLiveData<String>()
    var saveTransaction: LiveData<String> = _saveTransaction

    fun get(id: Int) {
        _dataBaseId.value = repository.getForId(id)
    }

    fun save(transaction: TransactionModel) {

        viewModelScope.launch {
            try {
                if (transaction.id == 0) {
                    if (repository.insert(transaction)) {
                        _saveTransaction.value = "Inserção com sucesso!"
                    } else {
                        _saveTransaction.value = "Falha"
                    }
                } else {
                    if (repository.update(transaction)) {
                        _saveTransaction.value = "Atualização com sucesso!"
                    } else {
                        _saveTransaction.value = "Falha"
                    }
                }
            } catch (e: Exception) {
                //TODO TRATAR ERRO
            }
        }
    }
}

class NewTransactionViewModelFactory(private val transactionsRepository: TransactionsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NewTransactionsViewlModel(transactionsRepository) as T
    }

}
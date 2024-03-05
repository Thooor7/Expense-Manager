package com.alterpat.budgettracker.viewmodel

import android.app.Application
import android.media.CamcorderProfile.getAll
import androidx.lifecycle.AndroidViewModel
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

    private val _saveGuest = MutableLiveData<String>()
    var saveGuest: LiveData<String> = _saveGuest

    fun get(id: Int) {
        _dataBaseId.value = repository.getForId(id)
    }

    fun save(guest: TransactionModel) {

        viewModelScope.launch {
            try {
                if (guest.id == 0) {
                    if (repository.insert(guest)) {
                        _saveGuest.value = "Inserção com sucesso!"
                    } else {
                        _saveGuest.value = "Falha"
                    }
                } else {
                    if (repository.update(guest)) {
                        _saveGuest.value = "Atualização com sucesso!"
                    } else {
                        _saveGuest.value = "Falha"
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
package com.alterpat.budgettracker.views

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.alterpat.budgettracker.TransactionsRepository
import com.alterpat.budgettracker.data.AppDataBase
import com.alterpat.budgettracker.data.TransactionModel
import com.alterpat.budgettracker.databinding.ActivityAddTransactionBinding
import com.alterpat.budgettracker.viewmodel.MainViewModelFactory
import com.alterpat.budgettracker.viewmodel.NewTransactionViewModelFactory
import com.alterpat.budgettracker.viewmodel.NewTransactionsViewlModel
import com.alterpat.budgettracker.viewmodel.TransactionsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityAddTransactionBinding
    private val binding get() = _binding
    private lateinit var viewModel: NewTransactionsViewlModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = NewTransactionViewModelFactory(TransactionsRepository(applicationContext))
        viewModel = ViewModelProvider(this, factory).get(NewTransactionsViewlModel::class.java)

        observer()
        InputMethodManager()
        textChangeListener()
        addTRansaction()
    }

    fun textChangeListener(){
        binding.labelInput.addTextChangedListener{
            if(it!!.count() > 0)
                binding.labelLayout.error = null
        }
        binding.amountInput.addTextChangedListener{
            if(it!!.count() > 0)
                binding.amountLayout.error = null
        }
    }

    fun addTRansaction(){
        binding.addTransactionBtn.setOnClickListener{
            val label = binding.labelInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()
            val description = binding.descriptionInput.text.toString()

            if(label.isEmpty())
                binding.labelLayout.error = "Please enter a valid label"

            else if(amount == null){
                binding.amountLayout.error = "please enter a valid amount"
            }
            else {
                val transactionModel =
                    TransactionModel().apply {
                        this.id = 0
                        this.label = label
                        this.amount = amount
                        this.description = description
                    }
                insert(transactionModel)
            }
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    fun InputMethodManager(){
        binding.rootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    fun observer(){
        viewModel.dataBaseId.observe(this, Observer {
            binding.labelInput.setText(it.label)
            binding.amountInput.setText(it.amount.toString())
            binding.descriptionInput.setText(it.description)
        })

        viewModel.saveGuest.observe(this, Observer {
            if(it != "") {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }

    private fun insert(transaction: TransactionModel) {

        viewModel.save(transaction)
        finish()
    }
}
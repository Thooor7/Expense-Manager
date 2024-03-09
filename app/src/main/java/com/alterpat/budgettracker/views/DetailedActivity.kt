package com.alterpat.budgettracker.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alterpat.budgettracker.TransactionsRepository
import com.alterpat.budgettracker.data.TransactionModel
import com.alterpat.budgettracker.databinding.ActivityDetailedBinding
import com.alterpat.budgettracker.viewmodel.NewTransactionViewModelFactory
import com.alterpat.budgettracker.viewmodel.NewTransactionsViewlModel
import java.text.SimpleDateFormat
import java.util.Date

class DetailedActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDetailedBinding
    private val binding get() = _binding
    private lateinit var transaction: TransactionModel
    private var transferId = 0
    private lateinit var viewModel: NewTransactionsViewlModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = NewTransactionViewModelFactory(TransactionsRepository(applicationContext))

        viewModel = ViewModelProvider(this, factory).get(NewTransactionsViewlModel::class.java)
        observer()
        loadData()

        binding.rootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.labelInput.addTextChangedListener {
            binding.updatebtn.visibility = View.VISIBLE
            if (it!!.count() > 0)
                binding.labelLayout.error = null
        }
        binding.amountInput.addTextChangedListener {
            binding.updatebtn.visibility = View.VISIBLE
            if (it!!.count() > 0)
                binding.amountLayout.error = null
        }
        binding.descriptionInput.addTextChangedListener {
            binding.updatebtn.visibility = View.VISIBLE
        }

        binding.updatebtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()
            val description = binding.descriptionInput.text.toString()

            if (label.isEmpty())
                binding.labelLayout.error = "Please enter a valid label"
            else if (amount == null) {
                binding.amountLayout.error = "please enter a valid amount"
            } else {
                if (!::transaction.isInitialized) {
                transaction = TransactionModel()
            }
                transaction = TransactionModel().apply {
                    this.id = transferId
                    this.label = label
                    this.amount = amount
                    this.description = description
                    this.date = getCurrentDate()
                }
                update(transaction)
            }
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }


    }

    fun observer(){
        viewModel.dataBaseId.observe(this, Observer {
            binding.labelInput.setText(it.label)
            binding.amountInput.setText(it.amount.toString())
            binding.descriptionInput.setText(it.description)
        })

        viewModel.saveTransaction.observe(this, Observer {
            if(it != "") {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }

    private fun loadData(){
        val bundle = intent.extras
        if(bundle != null){
            transferId = bundle.getInt("transaction")
            viewModel.get(transferId)
        }
    }

    private fun update(transaction: TransactionModel) {
        viewModel.save(transaction)
        finish()
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormat.format(Date())

        return currentDate.replace("-", "/")
    }

}

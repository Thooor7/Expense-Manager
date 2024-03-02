package com.alterpat.budgettracker.views

import android.content.Context
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.alterpat.budgettracker.R
import com.alterpat.budgettracker.data.AppDataBase
import com.alterpat.budgettracker.data.Transaction
import com.alterpat.budgettracker.databinding.ActivityAddTransactionBinding
import com.alterpat.budgettracker.databinding.ActivityDetailedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDetailedBinding
    private val binding get() = _binding
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        binding.labelInput.setText(transaction.label)
        binding.amountInput.setText(transaction.amount.toString())
        binding.descriptionInput.setText(transaction.description)

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
                transaction = Transaction(transaction.id, label, amount, description)
                update(transaction)
            }
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun update(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDataBase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}

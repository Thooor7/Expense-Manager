package com.alterpat.budgettracker.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.alterpat.budgettracker.data.AppDataBase
import com.alterpat.budgettracker.data.Transaction
import com.alterpat.budgettracker.databinding.ActivityAddTransactionBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityAddTransactionBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.labelInput.addTextChangedListener{
            if(it!!.count() > 0)
                binding.labelLayout.error = null
        }
        binding.amountInput.addTextChangedListener{
            if(it!!.count() > 0)
                binding.amountLayout.error = null
        }

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
                val transaction = Transaction(0, label, amount, description)
                insert(transaction)
            }
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDataBase::class.java,
            "transactions").build()

        GlobalScope.launch{
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}
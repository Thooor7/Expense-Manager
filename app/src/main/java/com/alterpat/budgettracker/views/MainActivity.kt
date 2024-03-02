package com.alterpat.budgettracker.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.alterpat.budgettracker.R
import com.alterpat.budgettracker.adapter.TransactionAdapter
import com.alterpat.budgettracker.data.AppDataBase
import com.alterpat.budgettracker.data.Transaction
import com.alterpat.budgettracker.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var oldTransaction: List<Transaction>
    private lateinit var deletedTransaction: Transaction
    private lateinit var transactions: List<Transaction>
    private lateinit var adapter: TransactionAdapter
    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    private lateinit var db: AppDataBase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactions = arrayListOf()

        db = Room.databaseBuilder(this,
            AppDataBase::class.java,
            "transactions").build()

        adapter = TransactionAdapter(transactions)
        binding.recycleview.layoutManager = LinearLayoutManager(applicationContext)
        binding.recycleview.adapter = adapter

        onClickNewTransaction()
        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(binding.recycleview)
    }


    //swipe to remove

    val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            deleteTransaction(transactions[viewHolder.adapterPosition])
        }

    }



    private fun fetchAll(){
        GlobalScope.launch {
            transactions = db.transactionDao().getAll()

            runOnUiThread{
                updateDashboard()
                adapter.setData(transactions)
            }
        }
    }

    private fun updateDashboard(){
        val totalAmount = transactions.map { it.amount }.sum()
        val budgetAmount = transactions.filter { it.amount>0 }.map { it.amount }.sum()
        val expenseAmount = totalAmount - budgetAmount

        binding.balance.text = "$ %.2f".format(totalAmount)
        binding.budget.text = "$ %.2f".format(budgetAmount)
        binding.expense.text = "$ %.2f".format(expenseAmount)
    }

    private fun undoDelete(){
        GlobalScope.launch {
            db.transactionDao().insertAll(deletedTransaction)

            transactions = oldTransaction
            runOnUiThread {
                adapter.setData(transactions)
                updateDashboard()
            }
        }
    }

    private fun showSnackbar(){
        val view = binding.coordinator
        val snackbar = Snackbar.make(view, "transaction deleted!", Snackbar.LENGTH_LONG )
        snackbar.setAction("Undo"){
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    fun deleteTransaction(transaction: Transaction){
        deletedTransaction = transaction
        oldTransaction = transactions

        GlobalScope.launch {
            db.transactionDao().delete(transaction)

            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread{
                updateDashboard()
                adapter.setData(transactions)
                showSnackbar()
            }
        }
    }


    fun onClickNewTransaction(){
        binding.newTransaction.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        fetchAll()
    }

}
package com.alterpat.budgettracker.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alterpat.budgettracker.R
import com.alterpat.budgettracker.TransactionsRepository
import com.alterpat.budgettracker.adapter.TransactionAdapter
import com.alterpat.budgettracker.data.TransactionModel
import com.alterpat.budgettracker.databinding.ActivityMainBinding
import com.alterpat.budgettracker.viewmodel.MainViewModelFactory
import com.alterpat.budgettracker.viewmodel.TransactionsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var transactions: MutableList<TransactionModel>
    private lateinit var adapter: TransactionAdapter
    private lateinit var _binding: ActivityMainBinding
    private lateinit var viewModel: TransactionsViewModel

    private var deletedPosition: Int = -1
    private lateinit var removedItem: TransactionModel
    private var isUndoClicked: Boolean = false

    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = MainViewModelFactory(TransactionsRepository(applicationContext))

        viewModel = ViewModelProvider(this, factory).get(TransactionsViewModel::class.java)
        transactions = arrayListOf()

        viewModel.getAll()
        observer()

        adapter = TransactionAdapter(this, transactions)
        binding.recycleview.layoutManager = LinearLayoutManager(applicationContext)
        binding.recycleview.adapter = adapter

        onClickNewTransaction()
//        val swipeHelper = ItemTouchHelper(itemTouchHelper)
//        swipeHelper.attachToRecyclerView(binding.recycleview)

        newSwipeToRemove().attachToRecyclerView(binding.recycleview)

    }


    //swipe to remove

    fun newSwipeToRemove(): ItemTouchHelper {
        val itemTouchHelper = object : SwiperGesture(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (transactions.isNotEmpty()) {
                    deletedPosition = viewHolder.adapterPosition
                    removedItem = transactions.removeAt(viewHolder.adapterPosition)
                    adapter.setData(transactions)

                    showSnackbar()
                }
            }
        }
        return ItemTouchHelper(itemTouchHelper)
    }


    fun swipeToRemove(): ItemTouchHelper {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (transactions.isNotEmpty()) {
                    deletedPosition = viewHolder.adapterPosition
                    removedItem = transactions.removeAt(viewHolder.adapterPosition)
                    adapter.setData(transactions)

                    showSnackbar()
                }
            }

        }
        return ItemTouchHelper(itemTouchHelper)
    }

    fun observer(){
        viewModel.budgetDataBaseLocal.observe(this, Observer {
            transactions.clear()
            transactions.addAll(it)
            adapter.setData(transactions)
            updateDashboard(it)
        })
    }

    private fun updateDashboard(data: List<TransactionModel>){
        val totalAmount = data.map { it.amount }.sum()
        val budgetAmount = data.filter { it.amount>0 }.map { it.amount }.sum()
        val expenseAmount = totalAmount - budgetAmount

        binding.balance.text = "R$ %.2f".format(totalAmount)
        binding.budget.text = "R$ %.2f".format(budgetAmount)
        binding.expense.text = "R$ %.2f".format(expenseAmount)
    }

    fun deleteTransaction(transactionModel: TransactionModel) {
        GlobalScope.launch {
            viewModel.delete(transactionModel.id)
        }
    }

    private fun showSnackbar() {
        val view = binding.coordinator
        val snackbar = Snackbar.make(view, "Transação deletada!", Snackbar.LENGTH_LONG)
        snackbar.setAction("Voltar") {
            isUndoClicked = true // Usuário clicou em "Voltar"
            // Desfaz a exclusão adicionando o item de volta à posição original
            if (deletedPosition != -1) {
                transactions.add(deletedPosition, this.removedItem)
                adapter.notifyDataSetChanged()
            }
            deletedPosition = -1 // Redefine a posição excluída
        }
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (!isUndoClicked) {
                    // Se o usuário não clicou em "Voltar", exclua permanentemente
                    deleteTransaction(removedItem)
                }
                isUndoClicked = false // Redefine a variável
            }
        })
        snackbar.show()
    }

    fun onClickNewTransaction(){
        binding.newTransaction.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }

}
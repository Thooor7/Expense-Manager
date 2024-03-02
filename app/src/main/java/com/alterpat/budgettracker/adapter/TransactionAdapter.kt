package com.alterpat.budgettracker.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alterpat.budgettracker.R
import com.alterpat.budgettracker.data.Transaction
import com.alterpat.budgettracker.views.DetailedActivity

class TransactionAdapter(private var transactions: List<Transaction>
): RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout, parent, false)
        return TransactionHolder(view)

    }

    override fun getItemCount(): Int {
        return transactions.size

    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.amount.context

        if (transaction.amount >= 0){
            holder.amount.text = "+ $%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            holder.amount.text = "- $%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        holder.label.text = transaction.label

        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("transaction", transaction)
            context.startActivity(intent)
        }
    }

    fun setData(transactions: List<Transaction>){
        this.transactions = transactions
        notifyDataSetChanged()
    }

    class TransactionHolder(view: View): RecyclerView.ViewHolder(view){
        val label: TextView = view.findViewById(R.id.label)
        val amount: TextView = view.findViewById(R.id.amount)


    }
}

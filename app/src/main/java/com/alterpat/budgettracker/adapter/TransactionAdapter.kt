package com.alterpat.budgettracker.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alterpat.budgettracker.R
import com.alterpat.budgettracker.data.TransactionModel
import com.alterpat.budgettracker.views.DetailedActivity

class TransactionAdapter(private val activity: Activity, private var transactionModels: List<TransactionModel, >
): RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout, parent, false)
        return TransactionHolder(view)

    }

    override fun getItemCount(): Int {
        return transactionModels.size

    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactionModels[position]
        val context = holder.amount.context

        if (transaction.amount >= 0) {
            holder.amount.text = "+ R$%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            val formattedAmount = "R$%.2f".format(Math.abs(transaction.amount))
            holder.amount.text = "- $formattedAmount"
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        holder.label.text = transaction.label

        holder.itemView.setOnClickListener{
            val intent = Intent(context.applicationContext, DetailedActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("transaction", transaction.id)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    fun setData(list: List<TransactionModel>){
        this.transactionModels = list
        notifyDataSetChanged()
    }

    class TransactionHolder(view: View): RecyclerView.ViewHolder(view){
        val label: TextView = view.findViewById(R.id.label)
        val amount: TextView = view.findViewById(R.id.amount)


    }
}

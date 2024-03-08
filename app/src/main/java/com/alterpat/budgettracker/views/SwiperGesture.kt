package com.alterpat.budgettracker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.TypedValue.COMPLEX_UNIT_SP
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alterpat.budgettracker.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwiperGesture(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    val deleteColor = ContextCompat.getColor(context, R.color.redLight)
    val deleteIcon = R.drawable.ic_exclude
    val deleteText = "Excluir"

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(
            c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        )
            .addSwipeRightBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(deleteIcon)
            .addSwipeRightLabel(deleteText)
            .setSwipeRightLabelTypeface(Typeface.DEFAULT_BOLD)
            .setSwipeRightLabelTextSize(COMPLEX_UNIT_SP, 16F)
            .setSwipeRightLabelColor(R.color.black)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


}
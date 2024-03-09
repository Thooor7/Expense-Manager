package com.alterpat.budgettracker.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
class TransactionModel() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "label")
    var label: String = ""

    @ColumnInfo(name = "amount")
    var amount: Double = 0.0

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "date")
    var date: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        label = parcel.readString().toString()
        amount = parcel.readDouble()
        description = parcel.readString().toString()
        date = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(label)
        parcel.writeDouble(amount)
        parcel.writeString(description)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionModel> {
        override fun createFromParcel(parcel: Parcel): TransactionModel {
            return TransactionModel(parcel)
        }

        override fun newArray(size: Int): Array<TransactionModel?> {
            return arrayOfNulls(size)
        }
    }
}
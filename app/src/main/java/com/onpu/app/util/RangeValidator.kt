package com.onpu.app.util

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints

class RangeValidator(
    private val min: Long,
    private val max: Long
) : CalendarConstraints.DateValidator {

    constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readLong())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(min)
        dest.writeLong(max)
    }

    override fun isValid(date: Long) = !(min > date || max < date)

    companion object CREATOR : Parcelable.Creator<RangeValidator> {
        override fun createFromParcel(parcel: Parcel): RangeValidator {
            return RangeValidator(parcel)
        }

        override fun newArray(size: Int): Array<RangeValidator?> {
            return arrayOfNulls(size)
        }
    }
}
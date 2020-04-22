package com.codeerow.sandbox.layout_manager.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


//TODO: there is an issue when rotating and scroll till the end
@Parcelize
data class ItemsPositionState(
    var firstVisiblePosition: Int,
    var lastVisiblePosition: Int
) : Parcelable
package com.raywenderlich.newsapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class Source(
    val id: String,
    val name: String
): Serializable
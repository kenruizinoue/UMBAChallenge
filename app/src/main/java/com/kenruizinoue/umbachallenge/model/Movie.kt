package com.kenruizinoue.umbachallenge.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int?,
    val id: Int,
    val backdrop_path: String?,
    val title: String?,
    val vote_average: String?,
    val overview: String?,
    val release_date: String?,
    var type: String?
): Parcelable
package com.jay.moviesdemo.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "movies_table")
@Parcelize
data class Movie(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var mId: Int = 0,

    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    var movieId: Int? = null,

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    var originalTitle: String? = null,

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var overview: String? = null,

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    var voteAverage: Double? = null,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    var releaseDate: String? = null,

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String? = null,

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    var popularity: Double? = null,

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    var voteCount: Long? = null,

    @ColumnInfo(name = "timestamp_in_millis")
    @SerializedName("timestamp_in_millis")
    var timestampInMillis: Long? = null
) : Parcelable
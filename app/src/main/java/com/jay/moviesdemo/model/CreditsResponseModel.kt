package com.jay.moviesdemo.model

class CreditsResponseModel(
    val id: Int? = null,
    val cast: List<Cast>? = null,
    val crew: List<Crew>? = null
) : NetworkResponseModel
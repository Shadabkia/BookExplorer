package com.kia.bookexplorer.data.network.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookResponse(
    @Json(name = "docs")
    val books: List<Book>,
    @Json(name = "numFound")
    val numFound: Int?,
    @Json(name = "num_found")
    val num_Found: Int?,
    @Json(name = "numFoundExact")
    val numFoundExact: Boolean?,
    @Json(name = "offset")
    val offset: Any?,
    @Json(name = "q")
    val q: String?,
    @Json(name = "start")
    val start: Int?
)
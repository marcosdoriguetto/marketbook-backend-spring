package com.marketbook.controller.response

import com.marketbook.model.BookModel
import com.marketbook.model.CustomerModel
import java.math.BigDecimal

data class SoldBooksResponse(
    val customer: CustomerModel,
    val books: List<BookModel>,
    val booksSold: Int,
    val totalValueBooksSold: BigDecimal
)

package com.marketbook.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.marketbook.model.BookModel
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PostPurchaseRequest(
    @field:NotNull
    @field:Positive
    @JsonAlias("customer_id")
    var customerId: Int,
    @field: NotNull
    @JsonAlias("book_ids")
    var booksIds: Set<Int>
)

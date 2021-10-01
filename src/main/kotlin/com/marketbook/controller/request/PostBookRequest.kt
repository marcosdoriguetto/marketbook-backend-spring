package com.marketbook.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class PostBookRequest(
    @field: NotEmpty(message = "É necessário informar um nome.")
    var name: String,
    @field: NotNull(message = "É necessário informar um preço.")
    var price: BigDecimal,
    @JsonAlias("customer_id")
    var customerId: Int
)

package com.marketbook.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PutCustomerRequest(
    @field: NotEmpty(message = "É necessário informar um nome.")
    var name: String,
    @field: Email(message = "E-mail inválido.")
    var email: String
)
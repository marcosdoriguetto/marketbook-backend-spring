package com.marketbook.controller.request

import com.marketbook.validation.EmailAvailable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PostCustomerRequest(
    @field: NotEmpty(message = "É necessário informar um nome.")
    var name: String,
    @field: Email(message = "E-mail inválido.")
    @EmailAvailable
    var email: String,
    @field: NotEmpty(message = "A senha deve ser informada")
    var password: String
)
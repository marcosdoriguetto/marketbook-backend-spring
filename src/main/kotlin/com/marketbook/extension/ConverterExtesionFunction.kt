package com.marketbook.extension

import com.marketbook.controller.request.PostBookRequest
import com.marketbook.controller.request.PostCustomerRequest
import com.marketbook.controller.request.PutCustomerRequest
import com.marketbook.enums.BookStatus
import com.marketbook.model.BookModel
import com.marketbook.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel =
    CustomerModel(name = this.name, email = this.email)

fun PutCustomerRequest.toCustomerModel(id: Int): CustomerModel =
    CustomerModel(id = id, name = this.name, email = this.email)

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
    return BookModel(
        name = this.name,
        price = this.price,
        status = BookStatus.ACTIVE,
        customer = customer
    )
}
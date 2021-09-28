package com.marketbook.extension

import com.marketbook.controller.request.PostBookRequest
import com.marketbook.controller.request.PostCustomerRequest
import com.marketbook.controller.request.PutBookRequest
import com.marketbook.controller.request.PutCustomerRequest
import com.marketbook.controller.response.BookResponse
import com.marketbook.controller.response.CustomerResponse
import com.marketbook.enums.BookStatus
import com.marketbook.enums.CustomerStatus
import com.marketbook.model.BookModel
import com.marketbook.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel =
    CustomerModel(name = this.name, email = this.email, status = CustomerStatus.ACTIVE)

fun PutCustomerRequest.toCustomerModel(previousCustomer: CustomerModel): CustomerModel =
    CustomerModel(id = previousCustomer.id, name = this.name, email = this.email, status = previousCustomer.status)

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
    return BookModel(
        name = this.name,
        price = this.price,
        status = BookStatus.ACTIVE,
        customer = customer
    )
}

fun PutBookRequest.toBookModel(previousBook: BookModel): BookModel {
    return BookModel(
        id = previousBook.id,
        name = this.name ?: previousBook.name,
        price = this.price ?: previousBook.price,
        status = previousBook.status,
        customer = previousBook.customer
    )
}

fun CustomerModel.toResponse(): CustomerResponse {
    return CustomerResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        status = this.status
    )
}

fun BookModel.toResponse(): BookResponse {
    return BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        customer = this.customer,
        status = this.status
    )
}
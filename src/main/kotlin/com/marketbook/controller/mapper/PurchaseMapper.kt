package com.marketbook.controller.mapper

import com.marketbook.controller.request.PostPurchaseRequest
import com.marketbook.model.PurchaseModel
import com.marketbook.service.BookService
import com.marketbook.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {
    fun toModel(request: PostPurchaseRequest): PurchaseModel {
        val customer = customerService.getCustomerById(request.customerId)
        val books = bookService.findAllByIds(request.booksIds)

        return PurchaseModel(
            customer = customer,
            books = books.toMutableList(),
            price = books.sumOf { it.price }
        )
    }
}
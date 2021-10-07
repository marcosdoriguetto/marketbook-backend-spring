package com.marketbook.service

import com.marketbook.controller.response.SoldBooksResponse
import com.marketbook.enums.BookStatus
import com.marketbook.enums.Errors
import com.marketbook.events.PurchaseEvent
import com.marketbook.exception.BadRequestException
import com.marketbook.model.PurchaseModel
import com.marketbook.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val bookService: BookService,
    private val customerService: CustomerService
) {
    fun create(purchaseModel: PurchaseModel) {
        purchaseModel.books.map {
            if(it.status != BookStatus.ACTIVE) {
                throw BadRequestException(Errors.ML102.message.format(it.status), Errors.ML102.code)
            }
        }

        purchaseRepository.save(purchaseModel)
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel))
    }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }

    fun getBooksSold(id: Int): SoldBooksResponse {
        val customer = customerService.getCustomerById(id)
        val booksCustomerSold = bookService.findByCustomer(customer).filter { it.status == BookStatus.SOLD }
        val totalBooksSold = booksCustomerSold.size
        val totalValueBooks = booksCustomerSold.sumOf {
            it.price
        }

        return SoldBooksResponse(
            customer = customer,
            books = booksCustomerSold,
            booksSold = totalBooksSold,
            totalValueBooksSold = totalValueBooks
        )
    }
}
package com.marketbook.controller

import com.marketbook.controller.mapper.PurchaseMapper
import com.marketbook.controller.request.PostPurchaseRequest
import com.marketbook.controller.response.SoldBooksResponse
import com.marketbook.service.BookService
import com.marketbook.service.CustomerService
import com.marketbook.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("purchase")
class PurchaseController(
    private val purchaseService: PurchaseService,
    private val purchaseMapper: PurchaseMapper,
    private val bookService: BookService,
    private val customerService: CustomerService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPurchase(@RequestBody @Valid request: PostPurchaseRequest) {
        request.booksIds.map {
            bookService.findById(it)
        }

        purchaseService.create(purchaseMapper.toModel(request))
    }

    @GetMapping("/{id}")
    fun getBooksSoldByCustomerId(@PathVariable id: Int): SoldBooksResponse {
        customerService.getCustomerById(id)

        return purchaseService.getBooksSold(id)
    }
}
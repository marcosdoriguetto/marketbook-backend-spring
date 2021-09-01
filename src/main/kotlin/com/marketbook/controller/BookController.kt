package com.marketbook.controller

import com.marketbook.controller.request.PostBookRequest
import com.marketbook.extension.toBookModel
import com.marketbook.model.BookModel
import com.marketbook.service.BookService
import com.marketbook.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.awt.print.Book

@RestController
@RequestMapping("books")
class BookController(
    val bookService: BookService,
    val customerService: CustomerService
) {
    @GetMapping
    fun findAllBooks(): List<BookModel> =
        bookService.findAll()

    @GetMapping("/actives")
    fun findActivesBooks(): List<BookModel> =
        bookService.findAllActivesBooks()

    @GetMapping("/{id}")
    fun findBookById(@PathVariable id: Int): BookModel =
        bookService.findById(id)

    @PostMapping
    fun createBook(@RequestBody request: PostBookRequest) {
        val customer = customerService.getCustomerById(request.customerId)
        bookService.create(request.toBookModel(customer))
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int) =
        bookService.delete(id)
}
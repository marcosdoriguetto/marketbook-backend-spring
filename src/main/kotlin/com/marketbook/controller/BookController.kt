package com.marketbook.controller

import com.marketbook.controller.request.PostBookRequest
import com.marketbook.controller.request.PutBookRequest
import com.marketbook.controller.response.BookResponse
import com.marketbook.extension.toBookModel
import com.marketbook.extension.toResponse
import com.marketbook.service.BookService
import com.marketbook.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.data.domain.Pageable

@RestController
@RequestMapping("books")
class BookController(
    val bookService: BookService,
    val customerService: CustomerService
) {
    @GetMapping
    fun findAllBooks(@PageableDefault(page = 0, size = 10) pageable: Pageable, @RequestParam name: String?): Page<BookResponse> =
        bookService.findAll(pageable, name).map{ it.toResponse() }

    @GetMapping("/actives")
    fun findActivesBooks(): List<BookResponse> =
        bookService.findAllActivesBooks().map{ it.toResponse() }

    @GetMapping("/{id}")
    fun findBookById(@PathVariable id: Int): BookResponse =
        bookService.findById(id).toResponse()

    @PostMapping
    fun createBook(@RequestBody request: PostBookRequest) {
        val customer = customerService.getCustomerById(request.customerId)
        bookService.create(request.toBookModel(customer))
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateBook(@PathVariable id: Int, @RequestBody book: PutBookRequest) {
        val bookSaved = bookService.findById(id)
        bookService.update(book.toBookModel(bookSaved))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int) =
        bookService.delete(id)
}
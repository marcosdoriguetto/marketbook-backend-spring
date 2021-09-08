package com.marketbook.service

import com.marketbook.enums.BookStatus
import com.marketbook.model.BookModel
import com.marketbook.repository.BookRepository
import org.springframework.stereotype.Service
import java.awt.print.Book

@Service
class BookService(
    val bookRepository: BookRepository
) {
    fun create(book: BookModel) =
        bookRepository.save(book)

    fun findAll(): List<BookModel> =
        bookRepository.findAll().toList()

    fun findAllActivesBooks(): List<BookModel> =
        bookRepository.findByStatus(BookStatus.ACTIVE)

    fun findById(id: Int): BookModel =
        bookRepository.findById(id).orElseThrow()

    fun update(book: BookModel) {
        bookRepository.save(book)
    }

    fun delete(id: Int) {
        val book = findById(id);

        book.status = BookStatus.CANCELED
        update(book)
    }

}

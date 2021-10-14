package com.marketbook.service

import com.marketbook.enums.BookStatus
import com.marketbook.enums.Errors
import com.marketbook.exception.NotFoundException
import com.marketbook.model.BookModel
import com.marketbook.model.CustomerModel
import com.marketbook.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import java.awt.print.Book

@Service
class BookService(
    private val bookRepository: BookRepository
) {
    fun create(book: BookModel) =
        bookRepository.save(book)

    fun findAll(pageable: Pageable, name: String?): Page<BookModel> {
        name?.let{
            return bookRepository.findByNameContaining(pageable, it)
        }

        return bookRepository.findAll(pageable)
    }

    fun findAllActivesBooks(pageable: Pageable): Page<BookModel> =
        bookRepository.findByStatus(pageable, BookStatus.ACTIVE)

    fun findById(id: Int): BookModel =
        bookRepository.findById(id).orElseThrow{NotFoundException(Errors.ML101.message.format(id), Errors.ML101.code)}

    fun update(book: BookModel) {
        bookRepository.save(book)
    }

    fun delete(id: Int) {
        val book = findById(id);

        book.status = BookStatus.CANCELED
        update(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books: List<BookModel> = findByCustomer(customer)
        for(book in books) {
            book.status = BookStatus.DELETED
        }

        bookRepository.saveAll(books)
    }

    fun findByCustomer(customer: CustomerModel): List<BookModel> {
        return bookRepository.findByCustomer(customer)
    }

    fun findAllByIds(booksIds: Set<Int>): List<BookModel> {
        return bookRepository.findAllById(booksIds).toList()
    }

    fun purchase(books: MutableList<BookModel>) {
        books.map {
            it.status = BookStatus.SOLD
        }

        bookRepository.saveAll(books)
    }
}

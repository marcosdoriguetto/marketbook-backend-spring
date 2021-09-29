package com.marketbook.service

import com.marketbook.enums.BookStatus
import com.marketbook.exception.NotFoundException
import com.marketbook.model.BookModel
import com.marketbook.model.CustomerModel
import com.marketbook.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable

@Service
class BookService(
    val bookRepository: BookRepository
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
        bookRepository.findById(id).orElseThrow{NotFoundException("Book not exists. Id book: $id", "MB-0001")}

    fun update(book: BookModel) {
        bookRepository.save(book)
    }

    fun delete(id: Int) {
        val book = findById(id);

        book.status = BookStatus.CANCELED
        update(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books: List<BookModel> = bookRepository.findByCustomer(customer)
        for(book in books) {
            book.status = BookStatus.DELETED
        }

        bookRepository.saveAll(books)
    }
}

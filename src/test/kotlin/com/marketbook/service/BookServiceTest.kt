package com.marketbook.service

import com.marketbook.enums.BookStatus
import com.marketbook.enums.Errors
import com.marketbook.exception.NotFoundException
import com.marketbook.mocks.*
import com.marketbook.model.BookModel
import com.marketbook.repository.BookRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

@ExtendWith(MockKExtension::class)
class BookServiceTest {
    @MockK
    private lateinit var bookRepository: BookRepository
    @MockK
    private lateinit var pagesBook: Page<BookModel>
    @MockK
    private lateinit var pageable: Pageable

    @InjectMockKs
    @SpyK
    private lateinit var bookService: BookService

    @Test
    fun `test create book`() {
        every { bookRepository.save(any()) } returns mockBook

        bookService.create(mockBook)

        verify(exactly = 1) { bookRepository.save(any()) }
    }

    @Test
    fun `test find all books`() {
        every { bookRepository.findAll(any()) } returns pagesBook

        bookService.findAll(pageable, null)
        verify(exactly = 0) { bookRepository.findByNameContaining(any(), any()) }
        verify(exactly = 1) { bookRepository.findAll(any()) }
    }

    @Test
    fun `test find all books containing name`() {
        every { bookRepository.findByNameContaining(any(), any()) } returns pagesBook

        bookService.findAll(pageable, "a")

        verify(exactly = 1) { bookRepository.findByNameContaining(any(), any()) }
        verify(exactly = 0) { bookRepository.findAll(any()) }
    }

    @Test
    fun `test get all books by active status`() {
        every { bookRepository.findByStatus(any(), BookStatus.ACTIVE) } returns pagesBook

        bookService.findAllActivesBooks(pageable)
        verify(exactly = 1) { bookRepository.findByStatus(any(), BookStatus.ACTIVE) }
    }

    @Test
    fun `test find book by id`() {
        every { bookRepository.findById(any()) } returns Optional.of(mockBook)

        val book = bookService.findById(2)
        assertEquals("teste", book.name)

        verify(exactly = 1) { bookRepository.findById(any()) }
    }

    @Test
    fun `test find book id not found`() {
        every { bookRepository.findById(any()) } returns Optional.empty()

        val assertThrows = assertThrows<NotFoundException> {
            bookService.findById(1)
        }

        assertEquals(Errors.ML101.message.format(1), assertThrows.message)
    }

    @Test
    fun `test update book`() {
        every { bookRepository.save(any()) } returns mockBook

        bookService.update(mockBook)

        verify(exactly = 1) { bookRepository.save(any()) }
    }

    @Test
    fun `test delete book`() {
        every { bookRepository.findById(any()) } returns Optional.of(mockBook.copy(id = 10))
        every { bookRepository.save(any()) } returns mockBookCanceled

        bookService.delete(10)

        verify(exactly = 1) { bookRepository.findById(any()) }
        verify(exactly = 1) { bookRepository.save(any()) }
    }

    @Test
    fun `test delete book fail by not found book`() {
        every { bookRepository.findById(any()) } returns Optional.empty()

        val assertThrows = assertThrows<NotFoundException> {
            bookService.delete(5)
        }
        assertEquals(Errors.ML101.message.format(5), assertThrows.message)

        verify(exactly = 0) { bookRepository.save(mockBookDeleted) }
    }

/*    @Test
    fun `test delete book by customer delete`() {
        every { bookRepository.findByCustomer(any()) } returns listOf(mockBook, mockBookSold)
        every { bookRepository.saveAll(listOf(any())) } returns listOf(mockBook)
        bookService.deleteByCustomer(mockCustomer)

        verify(exactly = 1) { bookRepository.findByCustomer(any()) }
        verify(exactly = 1) { bookRepository.saveAll(listOf(any())) }
    }*/

    @Test
    fun `test find books by customer`() {
        every { bookRepository.findByCustomer(any()) } returns listOf(mockBook)

        bookService.findByCustomer(mockCustomer)

        verify(exactly = 1) { bookRepository.findByCustomer(any()) }
    }

    @Test
    fun `test find all books by id's`() {
        every { bookRepository.findAllById(any()) } returns listOf(mockBook)

        bookService.findAllByIds(setOf(1, 2, 3, 4))

        verify(exactly = 1) { bookRepository.findAllById(any()) }
    }

/*    @Test
    fun `test purchase books`() {
        every { bookRepository.saveAll(listOf(any())) } returns listOf(mockBook)

        bookService.purchase(mutableListOf(mockBook))

        verify(exactly = 1) { bookRepository.saveAll(listOf(any())) }
    }*/
}
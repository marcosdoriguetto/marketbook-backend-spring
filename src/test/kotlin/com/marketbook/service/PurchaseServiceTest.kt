package com.marketbook.service

import com.marketbook.enums.BookStatus
import com.marketbook.events.PurchaseEvent
import com.marketbook.exception.BadRequestException
import com.marketbook.exception.NotFoundException
import com.marketbook.mocks.*
import com.marketbook.repository.PurchaseRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import java.util.*

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {
    @MockK
    private lateinit var purchaseRepository: PurchaseRepository
    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher
    @MockK
    private lateinit var bookService: BookService
    @MockK
    @SpyK
    private lateinit var customerService: CustomerService
    @MockK
    private lateinit var customerRepository: PurchaseRepository

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService


    private val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun `test create purchase`() {
        every { purchaseRepository.save(any()) } returns mockPurchase
        every { applicationEventPublisher.publishEvent(any()) } returns Unit

        purchaseService.create(mockPurchase)

        verify(exactly = 1) { purchaseRepository.save(any()) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(mockPurchase, purchaseEventSlot.captured.purchaseModel)
    }

    @Test
    fun `test create purchase fail with book status other than active`() {
        val booksPurchase = mutableListOf(mockBook, mockBookSold, mockBook, mockBookCanceled)
        val purchase = mockPurchase.copy(books = booksPurchase)

        try {
            purchaseService.create(purchase)
            fail("Test not work")
        } catch (e: BadRequestException) {
            assertEquals("It is not possible to change the status of the book that has the status: ${BookStatus.SOLD}", e.message)
            verify(exactly = 0) { purchaseRepository.save(any()) }
            verify(exactly = 0) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }
        }
    }

    @Test
    fun `test update purchase`() {
        every { purchaseRepository.save(any()) } returns mockPurchase

        purchaseService.update(mockPurchase)

        verify(exactly = 1){ purchaseRepository.save(any()) }
    }

    @Test
    fun `test take books sold by customer`() {
        every { customerService.getCustomerById(any()) } returns mockCustomer
        every { bookService.findByCustomer(any()) } returns listOf(mockBook, mockBookSold, mockBookSold)

        val purchaseCustomer = purchaseService.getBooksSold(1)
        assertEquals(purchaseCustomer.booksSold, 2)

        verify(exactly = 1) { customerService.getCustomerById(any()) }
        verify(exactly = 1) { bookService.findByCustomer(any()) }
    }
}
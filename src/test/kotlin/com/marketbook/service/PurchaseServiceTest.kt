package com.marketbook.service

import com.marketbook.enums.Errors
import com.marketbook.events.PurchaseEvent
import com.marketbook.exception.BadRequestException
import com.marketbook.exception.NotFoundException
import com.marketbook.mocks.*
import com.marketbook.repository.CustomerRepository
import com.marketbook.repository.PurchaseRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkClass
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {
    private var purchaseRepository: PurchaseRepository? = null
    private var applicationEventPublisher: ApplicationEventPublisher? = null
    private var bookService: BookService? = null
    private var customerService: CustomerService? = null
    private var customerServiceSpy: CustomerService? = null
    private var bCryptPasswordEncoder: BCryptPasswordEncoder? = null
    private var customerRepository: CustomerRepository? = null
    
    private var purchaseService: PurchaseService? = null
    private var purchaseServiceSpy: PurchaseService? = null
    
    private val purchaseEventSlot = slot<PurchaseEvent>()

    @BeforeEach
    fun init() {
        purchaseRepository = mockkClass(PurchaseRepository::class)
        applicationEventPublisher = mockkClass(ApplicationEventPublisher::class)
        bookService = mockkClass(BookService::class)
        customerService = mockkClass(CustomerService::class)
        customerRepository = mockkClass(CustomerRepository::class)
        bCryptPasswordEncoder = mockkClass(BCryptPasswordEncoder::class)
        customerServiceSpy = CustomerService(customerRepository!!, bookService!!, bCryptPasswordEncoder!!)
        purchaseService = PurchaseService(purchaseRepository!!, applicationEventPublisher!!, bookService!!, customerService!!)
        purchaseServiceSpy = PurchaseService(purchaseRepository!!, applicationEventPublisher!!, bookService!!, customerServiceSpy!!)
    }
    @Test
    fun `test create purchase`() {
        every { purchaseRepository?.save(any()) } returns mockPurchase
        every { applicationEventPublisher?.publishEvent(any()) } returns Unit

        purchaseService?.create(mockPurchase)

        verify(exactly = 1) { purchaseRepository?.save(any()) }
        verify(exactly = 1) { applicationEventPublisher?.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(mockPurchase, purchaseEventSlot.captured.purchaseModel)
    }

    @Test
    fun `test create purchase fail with book status other than active`() {
        val booksPurchase = mutableListOf(mockBook, mockBookSold, mockBook, mockBookCanceled)
        val purchase = mockPurchase.copy(books = booksPurchase)

        val assertThrows = assertThrows<BadRequestException> {
            purchaseService?.create(purchase)
        }

        assertEquals(Errors.ML102.message.format("SOLD"), assertThrows.message)
    }

    @Test
    fun `test update purchase`() {
        every { purchaseRepository?.save(any()) } returns mockPurchase

        purchaseService?.update(mockPurchase)

        verify(exactly = 1){ purchaseRepository?.save(any()) }
    }

    @Test
    fun `test take books sold by customer`() {
        every { customerService?.getCustomerById(any()) } returns mockCustomer
        every { bookService?.findByCustomer(any()) } returns listOf(mockBook, mockBookSold, mockBookSold)

        val purchaseCustomer = purchaseService?.getBooksSold(1)
        assertEquals(purchaseCustomer?.booksSold, 2)

        verify(exactly = 1) { customerService?.getCustomerById(any()) }
        verify(exactly = 1) { bookService?.findByCustomer(any()) }
    }

    @Test
    fun `test take books sold by customer fail with customer not found`() {
        every { customerRepository?.findById(any()) } returns Optional.empty()

        val assertThrows = assertThrows<NotFoundException> {
            purchaseServiceSpy?.getBooksSold(1)
        }
        assertEquals(Errors.ML201.message.format(1), assertThrows.message)
    }
}
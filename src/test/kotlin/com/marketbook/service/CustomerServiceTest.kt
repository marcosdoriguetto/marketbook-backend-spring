package com.marketbook.service

import com.marketbook.enums.CustomerStatus
import com.marketbook.enums.Errors
import com.marketbook.exception.NotFoundException
import com.marketbook.mocks.mockCustomer
import com.marketbook.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    @MockK
    private lateinit var customerRepository: CustomerRepository
    @MockK
    private lateinit var bookService: BookService
    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `test show return all customers`() {
        every { customerRepository.findAll() } returns listOf(mockCustomer)

        val customers = customerService.getAll(null)
        assertEquals(customers, listOf(mockCustomer))

        verify(exactly = 1) { customerRepository.findAll() }
        verify(exactly = 0) { customerRepository.findByNameContaining(any()) }
    }

    @Test
    fun `test show return all customers by name`() {
        every { customerRepository.findAll() } returns listOf(mockCustomer)
        every { customerRepository.findByNameContaining(any()) } returns listOf(mockCustomer)

        val customers = customerService.getAll("teste")
        assertEquals(customers, listOf(mockCustomer))

        verify(exactly = 1) { customerRepository.findByNameContaining(any()) }
        verify(exactly = 0) { customerRepository.findAll() }
    }

    @Test
    fun `test get customer by id`() {
        every { customerRepository.findById(any()) } returns Optional.of(mockCustomer)

        customerService.getCustomerById(1)

        verify(exactly = 1) { customerRepository.findById(any()) }
    }

    @Test
    fun `test get customer by id not found`() {
        every { customerRepository.findById(any()) } returns Optional.empty()

        val assertThrows = assertThrows<NotFoundException> {
            customerService.getCustomerById(1)
        }

        assertEquals(Errors.ML201.message.format(1), assertThrows.message)
    }

    @Test
    fun `test create customer`() {
        every { bCrypt.encode(any()) } returns "teste"
        every { customerRepository.save(any()) } returns mockCustomer.copy(status = CustomerStatus.ACTIVE)

        val saveCustomer = customerService.createCustomer(mockCustomer)
        assertEquals(CustomerStatus.ACTIVE, saveCustomer.status)

        verify(exactly = 1) { bCrypt.encode(any()) }
        verify(exactly = 1) { customerRepository.save(any()) }
    }

    @Test
    fun `test update customer`() {
        every { customerRepository.existsById(any()) } returns true
        every { customerRepository.save(any()) } returns mockCustomer

        customerService.updateCustomer(mockCustomer)

        verify(exactly = 1) { customerRepository.existsById(any()) }
        verify(exactly = 1) { customerRepository.save(any()) }
    }

    @Test
    fun `test update customer fail with not found customer by id`() {
        every { customerRepository.existsById(any()) } returns false

        val assertThrows = assertThrows<NotFoundException> {
            customerService.updateCustomer(mockCustomer)
        }

        assertEquals(Errors.ML201.message.format(1), assertThrows.message)
    }

    @Test
    fun `test delete customer`() {
        every { customerRepository.findById(any()) } returns Optional.of(mockCustomer)
        every { customerService.getCustomerById(any()) } returns mockCustomer
        every { bookService.deleteByCustomer(any()) } returns Unit
        every { customerRepository.save(any()) } returns mockCustomer.copy(status = CustomerStatus.INACTIVE)

        customerService.deleteCustomer(16)

        verify(exactly = 1) { customerService.getCustomerById(any()) }
        verify(exactly = 1) { bookService.deleteByCustomer(any()) }
        verify(exactly = 1) { customerRepository.save(any()) }
    }

    @Test
    fun `test delete customer fail with customer id not found`() {
        every { customerRepository.findById(any()) } returns Optional.empty()

        val assertThrows = assertThrows<NotFoundException> {
            customerService.deleteCustomer(16)
        }

        assertEquals(Errors.ML201.message.format(16), assertThrows.message)
    }

    @Test
    fun `test check email is available`() {
        every { customerRepository.existsByEmail(any()) } returns true

        val emailAvailable = customerService.emailAvailable(mockCustomer.email)
        assertTrue(!emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(any()) }
    }

    @Test
    fun `test check email is available but email already exists`() {
        every { customerRepository.existsByEmail(any()) } returns false

        val emailAvailable = customerService.emailAvailable(mockCustomer.email)
        assertTrue(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(any()) }
    }
}
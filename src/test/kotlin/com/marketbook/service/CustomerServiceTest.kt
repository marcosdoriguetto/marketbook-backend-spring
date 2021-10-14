package com.marketbook.service

import com.marketbook.enums.CustomerStatus
import com.marketbook.enums.Role
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

        val customer = customerService.getCustomerById(1)
        assertEquals(customer.name, "teste")
        assertEquals(customer.status, CustomerStatus.ACTIVE)
        assertEquals(customer.roles, setOf(Role.CUSTOMER))

        verify(exactly = 1) { customerRepository.findById(any()) }
    }

    @Test
    fun `test get customer by id not found`() {
        every { customerRepository.findById(any()) } returns Optional.empty()

        try {
            val customer = customerService.getCustomerById(1)
            assertEquals(customer, mockCustomer)
            fail("Test is not work")
        } catch (e: NotFoundException) {
            assertEquals("Customer not exists. Customer id = 1", e.message)
            verify(exactly = 1) { customerRepository.findById(any()) }
        }
    }

    @Test
    fun `test create customer`() {
        every { customerRepository.save(any()) } returns mockCustomer
        every { bCrypt.encode(any()) } returns "teste"

        val saveCustomer = customerService.createCustomer(mockCustomer)
        assertEquals(saveCustomer.password, "teste")
        assertEquals(saveCustomer.status, CustomerStatus.ACTIVE)
        assertEquals(saveCustomer.roles, setOf(Role.CUSTOMER))

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

        try {
            customerService.updateCustomer(mockCustomer)
            fail("This test is not work")
        } catch (e: NotFoundException) {
            assertEquals("Customer not exists. Customer id = 1", e.message)
            verify(exactly = 1) { customerRepository.existsById(any()) }
            verify(exactly = 0) { customerRepository.save(any()) }
        }
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

        try {
            customerService.deleteCustomer(16)
            fail("Test is not work")
        } catch (e: NotFoundException) {
            assertEquals("Customer not exists. Customer id = 16", e.message)
            verify(exactly = 1) { customerService.getCustomerById(any()) }
            verify(exactly = 0) { bookService.deleteByCustomer(any()) }
            verify(exactly = 0) { customerRepository.save(any()) }
        }
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
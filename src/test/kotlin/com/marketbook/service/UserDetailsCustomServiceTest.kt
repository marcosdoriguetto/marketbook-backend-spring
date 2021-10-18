package com.marketbook.service

import com.marketbook.exception.AuthenticationException
import com.marketbook.mocks.mockCustomer
import com.marketbook.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class UserDetailsCustomServiceTest {
    @MockK
    private lateinit var customerRepository: CustomerRepository

    @InjectMockKs
    private lateinit var userDetailsCustomService: UserDetailsCustomService

    @Test
    fun `test load user by username`(){
        every { customerRepository.findById(any()) } returns Optional.of(mockCustomer)

        userDetailsCustomService.loadUserByUsername("1")

        verify(exactly = 1) { customerRepository.findById(any()) }
    }

    @Test
    fun `test load user fail by username not exists`(){
        every { customerRepository.findById(any()) } returns Optional.empty()

        val assertThrows = assertThrows<AuthenticationException> {
            userDetailsCustomService.loadUserByUsername("1")
        }

        assertEquals("Usuário não encontrado", assertThrows.message)
    }
}
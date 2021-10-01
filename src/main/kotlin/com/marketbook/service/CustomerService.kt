package com.marketbook.service

import com.marketbook.enums.CustomerStatus
import com.marketbook.enums.Errors
import com.marketbook.exception.NotFoundException
import com.marketbook.model.CustomerModel
import com.marketbook.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val bookService: BookService
) {
    //GET
    fun getAll(name: String?): List<CustomerModel> {
        name?.let{
            return customerRepository.findByNameContaining(it)
        }
        return customerRepository.findAll().toList()
    }

    fun getCustomerById(id: Int): CustomerModel =
        customerRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code) }

    //POST
    fun createCustomer(customer: CustomerModel) =
        customerRepository.save(customer)

    //PUT
    fun updateCustomer(customer: CustomerModel) {
        if(!customerRepository.existsById(customer.id!!)) {
            throw Exception()
        }

        customerRepository.save(customer)
    }

    //DELETE
    fun deleteCustomer(id: Int) {
        val customer = getCustomerById(id)

        bookService.deleteByCustomer(customer)
        customer.status = CustomerStatus.INACTIVE
        customerRepository.save(customer)
    }

    fun emailAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email)
    }
}
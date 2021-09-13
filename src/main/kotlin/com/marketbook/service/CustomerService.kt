package com.marketbook.service

import com.marketbook.controller.request.PostCustomerRequest
import com.marketbook.controller.request.PutCustomerRequest
import com.marketbook.model.CustomerModel
import com.marketbook.repository.BookRepository
import com.marketbook.repository.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

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
        customerRepository.findById(id).orElseThrow()

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
        //customerRepository.deleteById(id)
    }
}
package com.marketbook.controller

import com.marketbook.controller.request.PostCustomerRequest
import com.marketbook.controller.request.PutCustomerRequest
import com.marketbook.controller.response.CustomerResponse
import com.marketbook.extension.toCustomerModel
import com.marketbook.extension.toResponse
import com.marketbook.security.UserCanOnlyAccessTheirOwnResource
import com.marketbook.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("customers")
class CustomerController(
    val customerService: CustomerService
) {
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getAll(@RequestParam name: String?): List<CustomerResponse> {
        return customerService.getAll(name).map{ it.toResponse() }
    }

    @GetMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    fun getCustomer(@PathVariable id: Int): CustomerResponse =
        customerService.getCustomerById(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody @Valid customer: PostCustomerRequest) =
        customerService.createCustomer(customer.toCustomerModel())

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @UserCanOnlyAccessTheirOwnResource
    fun updateCustomer(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        val customerSaved = customerService.getCustomerById(id)
        customerService.updateCustomer(customer.toCustomerModel(customerSaved))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @UserCanOnlyAccessTheirOwnResource
    fun deleteCustomer(@PathVariable id: Int) =
        customerService.deleteCustomer(id)
}
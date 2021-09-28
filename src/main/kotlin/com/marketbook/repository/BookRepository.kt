package com.marketbook.repository

import com.marketbook.enums.BookStatus
import com.marketbook.model.BookModel
import com.marketbook.model.CustomerModel
import org.springframework.data.repository.CrudRepository

interface BookRepository: CrudRepository<BookModel, Int> {
    fun findByStatus(status: BookStatus): List<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
    fun findByNameContaining(name: String): List<BookModel>
}
package com.marketbook.repository

import com.marketbook.enums.BookStatus
import com.marketbook.model.BookModel
import com.marketbook.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.repository.CrudRepository
import org.springframework.data.domain.Pageable

interface BookRepository: CrudRepository<BookModel, Int> {
    fun findByStatus(pageable: Pageable, status: BookStatus): Page<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
    fun findByNameContaining(pageable: Pageable, name: String): Page<BookModel>

    fun findAll(pageable: Pageable): Page<BookModel>
}
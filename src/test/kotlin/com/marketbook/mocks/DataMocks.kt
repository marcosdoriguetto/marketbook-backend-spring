package com.marketbook.mocks

import com.marketbook.enums.BookStatus
import com.marketbook.enums.CustomerStatus
import com.marketbook.enums.Role
import com.marketbook.model.BookModel
import com.marketbook.model.CustomerModel
import com.marketbook.model.PurchaseModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*

val mockCustomer = CustomerModel(
    id = 1,
    name = "teste",
    email = "${UUID.randomUUID()}@email.com",
    status = CustomerStatus.ACTIVE,
    password = "teste",
    roles = setOf(Role.CUSTOMER)
)

val mockBook = BookModel(
    id = 1,
    name = "teste",
    price = BigDecimal(1.11),
    customer = mockCustomer,
    status = BookStatus.ACTIVE
)

val mockBookSold = BookModel(
    id = 2,
    name = "teste",
    price = BigDecimal(1.11),
    customer = mockCustomer,
    status = BookStatus.SOLD
)

val mockBookCanceled = BookModel(
    id = 3,
    name = "teste",
    price = BigDecimal(1.11),
    customer = mockCustomer,
    status = BookStatus.CANCELED
)

val mockBookDeleted = BookModel(
    id = 3,
    name = "teste",
    price = BigDecimal(1.11),
    customer = mockCustomer,
    status = BookStatus.DELETED
)

val mockPurchase = PurchaseModel(
    id = 1,
    customer = mockCustomer,
    books = mutableListOf(mockBook),
    nfe = UUID.randomUUID().toString(),
    price = BigDecimal(111),
    createAt = LocalDateTime.now()
)

val mockMutableIterable: MutableIterable<BookModel> = mutableListOf(mockBook, mockBookSold)
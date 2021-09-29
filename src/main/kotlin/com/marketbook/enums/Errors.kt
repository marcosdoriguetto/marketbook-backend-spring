package com.marketbook.enums

enum class Errors(val code: String, val message: String) {
    ML101("ML-101", "Book not exists. Book id = %s"),
    ML201("ML-201", "Customer not exists. Customer id = %s")
}
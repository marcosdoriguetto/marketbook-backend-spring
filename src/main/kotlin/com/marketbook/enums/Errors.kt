package com.marketbook.enums

enum class Errors(val code: String, val message: String) {
    ML000("ML-000", "Unauthorized"),
    ML001("ML-001", "Invalid request"),
    ML101("ML-101", "Book not exists. Book id = %s"),
    ML102("ML-102", "It is not possible to change the status of the book that has the status: %s"),
    ML201("ML-201", "Customer not exists. Customer id = %s")
}
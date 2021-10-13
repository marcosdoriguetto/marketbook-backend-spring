package com.marketbook.controller

import com.marketbook.controller.mapper.PurchaseMapper
import com.marketbook.controller.request.PostPurchaseRequest
import com.marketbook.controller.response.CustomerResponse
import com.marketbook.controller.response.SoldBooksResponse
import com.marketbook.service.BookService
import com.marketbook.service.CustomerService
import com.marketbook.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("admin")
class AdminController() {
    @GetMapping("/report")
    fun getAll(): String {
        return "This is a Report. Only admin can see it!"
    }
}
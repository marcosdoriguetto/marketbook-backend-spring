package com.marketbook.controller.response

data class PageResponse<T>(
    var item: List<T>,
    var totalItems: Long,
    var totalPages: Int,
    var currentPage: Int
)


package com.marketbook.service

import com.marketbook.model.PurchaseModel
import com.marketbook.repository.PurchaseRepository
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository
) {
    fun create(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }
}
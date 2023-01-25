package com.civilcam.domainLayer.usecase.subscriptions

import com.civilcam.domainLayer.repos.SubscriptionsRepository

class SetExpiredSubscriptionUseCase(
    private val subscriptionsRepository: SubscriptionsRepository
) {
    operator fun invoke() = subscriptionsRepository.setExpiredSub()
}
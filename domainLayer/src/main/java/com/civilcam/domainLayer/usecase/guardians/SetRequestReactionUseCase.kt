package com.civilcam.domainLayer.usecase.guardians

import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.repos.GuardiansRepository

class SetRequestReactionUseCase(
    private val guardiansRepository: GuardiansRepository
) {
    suspend operator fun invoke(reaction: ButtonAnswer, personId: Int) =
        guardiansRepository.setRequestReaction(reaction, personId)
}
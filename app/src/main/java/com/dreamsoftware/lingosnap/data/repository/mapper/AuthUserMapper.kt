package com.dreamsoftware.lingosnap.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.dto.AuthUserDTO
import com.dreamsoftware.lingosnap.domain.model.AuthUserBO

internal class AuthUserMapper: IBrownieOneSideMapper<AuthUserDTO, AuthUserBO> {
    override fun mapInToOut(input: AuthUserDTO): AuthUserBO = with(input) {
        AuthUserBO(
            uid = uid,
            displayName = displayName.orEmpty(),
            email = email.orEmpty(),
            photoUrl = photoUrl.orEmpty()
        )
    }

    override fun mapInListToOutList(input: Iterable<AuthUserDTO>): Iterable<AuthUserBO> =
        input.map(::mapInToOut)
}
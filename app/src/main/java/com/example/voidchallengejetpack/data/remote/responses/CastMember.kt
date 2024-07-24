package com.example.voidchallengejetpack.data.remote.responses

data class CastMember(
    val name: String = "",
    val profilePath: String = "",
    val roles: List<RoleInfo> = emptyList()

)
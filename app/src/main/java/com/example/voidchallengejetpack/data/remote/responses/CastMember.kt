package com.example.voidchallengejetpack.data.remote.responses

data class CastMember(
    val name: String,
    val profile_path: String,
    val roles: List<RoleInfo>

)
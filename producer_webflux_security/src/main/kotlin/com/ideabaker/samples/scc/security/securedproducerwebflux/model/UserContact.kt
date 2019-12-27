package com.ideabaker.samples.scc.security.securedproducerwebflux.model

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-01-07 00:22
 */
data class UserContact(
    val id: String,
    val name: String,
    val email: String,
    val invited: Boolean
)
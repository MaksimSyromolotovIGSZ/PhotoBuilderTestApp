package com.example.photobuilder.domain.entity

data class Photo(
    val id: Int = 0,
    val uri: String,
    val createdAt: String,
    val tag: String?
)

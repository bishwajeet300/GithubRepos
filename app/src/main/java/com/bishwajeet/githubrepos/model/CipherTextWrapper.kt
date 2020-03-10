package com.bishwajeet.githubrepos.model

data class CipherTextWrapper(
    val ciphertext: ByteArray,
    val initializationVector: ByteArray
)
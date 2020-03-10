package com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.biometric

import android.content.Context
import com.bishwajeet.githubrepos.model.CipherTextWrapper
import javax.crypto.Cipher

interface CryptographyManager {

    /**
     * This method first gets or generates an instance of SecretKey and then initializes the Cipher
     * with the key. The secret key uses [ENCRYPT_MODE][Cipher.ENCRYPT_MODE] is used.
     */
    fun getInitializedCipherForEncryption(keyName: String): Cipher


    /**
     * This method first gets or generates an instance of SecretKey and then initializes the Cipher
     * with the key. The secret key uses [DECRYPT_MODE][Cipher.DECRYPT_MODE] is used.
     */
    fun getInitializedCipherForDecryption(keyName: String, initializationVector: ByteArray): Cipher


    /**
     * The Cipher created with [getInitializedCipherForEncryption] is used here
     */
    fun encryptData(plainText: String, cipher: Cipher): CipherTextWrapper


    /**
     * The Cipher created with [getInitializedCipherForDecryption] is used here
     */
    fun decryptData(cipherText: ByteArray, cipher: Cipher): String


    fun persistCipherTextWrapperToSharedPrefs(
        context: Context,
        cipherTextWrapper: CipherTextWrapper,
        fileName: String,
        prefKey: String,
        mode: Int
    )


    fun getCipherTextWrapperFromSharedPrefs(
        context: Context,
        fileName: String,
        prefKey: String,
        mode: Int
    ): CipherTextWrapper?
}


fun CryptographyManager(): CryptographyManager =
    CryptographyManagerImpl()
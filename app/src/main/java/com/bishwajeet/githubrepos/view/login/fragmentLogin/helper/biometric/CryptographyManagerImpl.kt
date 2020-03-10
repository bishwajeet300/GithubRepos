package com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.biometric

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.bishwajeet.githubrepos.model.CipherTextWrapper
import com.google.gson.Gson
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptographyManagerImpl :
    CryptographyManager {

    private val KEY_SIZE = 256
    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES


    override fun getInitializedCipherForEncryption(keyName: String): Cipher {

        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }


    override fun getInitializedCipherForDecryption(
        keyName: String,
        initializationVector: ByteArray
    ): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, initializationVector))
        return cipher
    }


    override fun encryptData(plainText: String, cipher: Cipher): CipherTextWrapper {
        val ciphertext = cipher.doFinal(plainText.toByteArray(Charset.forName("UTF-8")))
        return CipherTextWrapper(
            ciphertext,
            cipher.iv
        )
    }


    override fun decryptData(cipherText: ByteArray, cipher: Cipher): String {
        val plainText = cipher.doFinal(cipherText)
        return String(plainText, Charset.forName("UTF-8"))
    }


    override fun persistCipherTextWrapperToSharedPrefs(
        context: Context,
        cipherTextWrapper: CipherTextWrapper,
        fileName: String,
        prefKey: String,
        mode: Int
    ) {
        val json = Gson().toJson(cipherTextWrapper)
        context.getSharedPreferences(fileName, mode).edit().putString(prefKey, json).apply()
    }


    override fun getCipherTextWrapperFromSharedPrefs(
        context: Context,
        fileName: String,
        prefKey: String,
        mode: Int
    ): CipherTextWrapper? {
        val json = context.getSharedPreferences(fileName, mode).getString(prefKey, null)
        return Gson().fromJson(json, CipherTextWrapper::class.java)
    }


    private fun getCipher(): Cipher {
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }


    private fun getOrCreateSecretKey(keyName: String): SecretKey {

        //If secret key was previously created for that keyName, then return it
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null) //KeyStore must be loaded before accessing
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }

        //If secret key was not returned, create a new SecretKey for keyName
        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        paramsBuilder.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
            setInvalidatedByBiometricEnrollment(true)
            setUserAuthenticationRequired(true)
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }
}
package com.cl.gesturelock.util

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * @author Chenli
 * @Date 2021/1/5
 * @Description
 */
class DefaultLockHelper : AbstractLockHelper() {
    /**
     * 加盐（任意字符串,后转md5）
     */
    private var slatKey = "GestureLock"

    fun setSlatKey(slatKey: String) {
        this.slatKey = slatKey
    }

    /**
     * 加盐加密
     * @param content
     */
    override fun encrypt(content: String): String {
        val cipher: Cipher = Cipher.getInstance(CIPHER_MODE)
        val secretKey: SecretKey = SecretKeySpec(slatKey.md5().toByteArray(), ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encrypted: ByteArray = cipher.doFinal(content.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    /**
     * 解密
     * @param encryptContent
     */
    override fun decrypt(encryptContent: String): String {
        val cipher: Cipher = Cipher.getInstance(CIPHER_MODE)
        val secretKey: SecretKey = SecretKeySpec(slatKey.md5().toByteArray(), ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val content: ByteArray = Base64.decode(encryptContent, Base64.DEFAULT)
        val encrypted = cipher.doFinal(content)
        return String(encrypted)
    }

    companion object {
        private const val CIPHER_MODE = "AES/ECB/PKCS5Padding"
        private const val ALGORITHM = "AES"

        fun String.md5(): String {
            val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
            return bytes.hex().substring(8, 24)
        }

        private fun ByteArray.hex(): String {
            return joinToString("") { "%02X".format(it) }
        }
    }
}
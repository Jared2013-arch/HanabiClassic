import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

// AES encode
fun encrypt(key: ByteArray, data: ByteArray): ByteArray {
    val cipher = Cipher.getInstance("AES/ECB/NoPadding")
    val secretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    return cipher.doFinal(data)
}

// AES decode
fun decrypt(key: ByteArray, data: ByteArray): ByteArray {
    val cipher = Cipher.getInstance("AES/ECB/NoPadding")
    val secretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    return cipher.doFinal(data)
}


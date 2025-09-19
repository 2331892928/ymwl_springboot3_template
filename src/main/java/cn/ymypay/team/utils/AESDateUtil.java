package cn.ymypay.team.utils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
public class AESDateUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; // 注意：需要和前端保持一致
    private static final String CHARSET = "UTF-8";

    /**
     * AES 加密
     *
     * @param plainText 明文
     * @param key 密钥 (16, 24, 或 32 字节)
     * @param iv 偏移量 (16 字节)
     * @return 密文 (Base64 编码)
     * @throws Exception
     */
    public static String encrypt(String plainText, String key, String iv) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(CHARSET));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * AES 解密
     *
     * @param cipherText 密文 (Base64 编码)
     * @param key 密钥 (16, 24, 或 32 字节)
     * @param iv 偏移量 (16 字节)
     * @return 明文
     * @throws Exception
     */
    public static String decrypt(String cipherText, String key, String iv) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(CHARSET));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, CHARSET);
    }
}

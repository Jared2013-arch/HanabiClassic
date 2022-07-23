package cn.hanabi.loader.auth;

import com.eskid.annotation.Native;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
@Native
public class Check {

    public Check(){
    }

    public static @NotNull String getOriginal() {
        try{
            String toEncrypt = "EmoManIsGay" + System.getProperty("COMPUTERNAME") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static String getHWID() {
        String hwid = null;
        try {
            hwid = g(getOriginal());
        } catch (Exception ignored) {
        }
        return hwid;
    }

    public static @NotNull String g(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        text = Base64.getUrlEncoder().encodeToString(text.getBytes());
        //System.out.println(text);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes(StandardCharsets.UTF_8), 0, text.length());
        text = DigestUtils.shaHex(text);
        return text.toUpperCase();
    }

    public static void doCrash() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = null;
            try {
                unsafe = (Unsafe) field.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            Class<?> cacheClass = null;
            try {
                cacheClass = Class.forName("java.lang.Integer$IntegerCache");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Field cache = cacheClass.getDeclaredField("cache");
            long offset = unsafe.staticFieldOffset(cache);

            unsafe.putObject(Integer.getInteger("SkidSense.pub NeverDie"), offset, null);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

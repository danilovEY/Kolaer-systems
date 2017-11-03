package ru.kolaer.server.webportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Класс для работы с токенами.
 *
 * Created by danilovey on 02.08.2016.
 */
public class TokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);
    public static final String MAGIC_KEY = "obfuscate";

    /**
     * Создать токен
     * @param username логин
     * @param password пароль
     * @return токен
     */
    public static String createToken(String username, String password, String postfix) {
        long expires = System.currentTimeMillis() + 1000L * 60 * 60 * 24;

        StringBuilder tokenBuilder = new StringBuilder();
        try {
            tokenBuilder.append(java.util.Base64.getEncoder().encodeToString(username.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            logger.error("Невозможно кодировать слово: {}", username, e);
            tokenBuilder.append(username);
        }
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(TokenUtils.computeSignature(username,password, expires));
        tokenBuilder.append(":");
        tokenBuilder.append(postfix);

        return tokenBuilder.toString();
    }

    public static String createToken(UserDetails userDetails) {
        return TokenUtils.createToken(userDetails.getUsername(), userDetails.getPassword(), "SQL");
    }

    /**
     * Генерация секретной части токена.
     * @param username логин
     * @param password пароль
     * @param expires время истечения токена
     * @return хэш-код
     */
    public static String computeSignature(String username, String password, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(username);
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(Optional.ofNullable(password).orElse(TokenUtils.MAGIC_KEY + String.valueOf(expires)));
        signatureBuilder.append(":");
        signatureBuilder.append(TokenUtils.MAGIC_KEY);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }


    /**
     * Получить логин из токена.
     * @param authToken токен
     * @return логин
     */
    public static String getUserNameFromToken(String authToken) {
        if (null == authToken) {
            return null;
        }

        String userName = authToken.split(":")[0];
        try {
            return new String(java.util.Base64.getDecoder().decode(userName), "utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Получить логин из токена.
     * @param authToken токен
     * @return логин
     */
    public static boolean isLDAP(String authToken) {
        if (null == authToken || authToken.trim().isEmpty()) {
            return true;
        }

        String[] parts = authToken.split(":");
        return parts[3].equals("LDAP");
    }

    /**
     * Проверка токена на валидность.
     * @param authToken токен
     * @param userDetails пользователь
     * @return
     */
    public static boolean validateToken(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];

        if (expires < System.currentTimeMillis()) {
            return false;
        }
        return signature.equals(TokenUtils.computeSignature(userDetails.getUsername(), userDetails.getPassword(), expires));
    }

    public static boolean validateTokenLDAP(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];

        if (expires < System.currentTimeMillis()) {
            return false;
        }

        return signature.equals(TokenUtils.computeSignature(userDetails.getUsername(), null, expires));
    }

    public static String createTokenLDAP(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), null, "LDAP");
    }
}

package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Класс для работы с токенами.
 *
 * Created by danilovey on 02.08.2016.
 */
@Service
@Slf4j
public class TokenService {
    String MAGIC_KEY = "obfuscate";

    public TokenService(@Value("${secret_key}") String magicKey) {
        this.MAGIC_KEY = magicKey;
    }

    /**
     * Создать токен
     * @param username логин
     * @param password пароль
     * @return токен
     */
    public String createToken(String username, String password, String postfix) {
        long expires = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7;

        StringBuilder tokenBuilder = new StringBuilder();
        try {
            tokenBuilder.append(java.util.Base64.getEncoder().encodeToString(username.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            log.error("Невозможно кодировать слово: {}", username, e);
            tokenBuilder.append(username);
        }
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(computeSignature(username,password, expires));
        tokenBuilder.append(":");
        tokenBuilder.append(postfix);

        return tokenBuilder.toString();
    }

    public String createToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), userDetails.getPassword(), "SQL");
    }

    /**
     * Генерация секретной части токена.
     * @param username логин
     * @param password пароль
     * @param expires время истечения токена
     * @return хэш-код
     */
    public String computeSignature(String username, String password, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(username);
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(Optional.ofNullable(password).orElse(MAGIC_KEY + String.valueOf(expires)));
        signatureBuilder.append(":");
        signatureBuilder.append(MAGIC_KEY);

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
    public String getUserNameFromToken(String authToken) {
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
    public boolean isLDAP(String authToken) {
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
    public boolean validateToken(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];

        if (expires < System.currentTimeMillis()) {
            return false;
        }
        return signature.equals(computeSignature(userDetails.getUsername(), userDetails.getPassword(), expires));
    }

    public boolean validateTokenLDAP(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];

        if (expires < System.currentTimeMillis()) {
            return false;
        }

        return signature.equals(computeSignature(userDetails.getUsername(), null, expires));
    }

    public String createTokenLDAP(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), null, "LDAP");
    }
}

package ru.aston.decorator.decorators;

import ru.aston.decorator.Storage;
import ru.aston.decorator.core.StorageDecorator;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

public final class AesGcmEncryptionDecorator extends StorageDecorator {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int TAG_BITS = 128;
    private static final int IV_BYTES = 12;

    private final SecretKey key;
    private final SecureRandom random = new SecureRandom();

    public AesGcmEncryptionDecorator(Storage delegate, SecretKey key) {
        super(delegate);
        if (key == null) throw new IllegalArgumentException("key must not be null");
        this.key = key;
    }

    public static SecretKey generateKey(int bits) throws IOException {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(bits);
            return kg.generateKey();
        } catch (GeneralSecurityException e) {
            throw new IOException("Failed to generate AES key", e);
        }
    }

    @Override
    public void put(String keyName, byte[] data) throws IOException {
        byte[] iv = new byte[IV_BYTES];
        random.nextBytes(iv);

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            byte[] ciphertext = cipher.doFinal(data);

            byte[] packed = new byte[IV_BYTES + ciphertext.length];
            System.arraycopy(iv, 0, packed, 0, IV_BYTES);
            System.arraycopy(ciphertext, 0, packed, IV_BYTES, ciphertext.length);

            super.put(keyName, packed);
        } catch (GeneralSecurityException e) {
            throw new IOException("Encryption failed", e);
        }
    }

    @Override
    public byte[] get(String keyName) throws IOException {
        byte[] packed = super.get(keyName);
        if (packed.length < IV_BYTES) {
            throw new IOException("Corrupted ciphertext: too short");
        }
        byte[] iv = Arrays.copyOfRange(packed, 0, IV_BYTES);
        byte[] ciphertext = Arrays.copyOfRange(packed, IV_BYTES, packed.length);

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            return cipher.doFinal(ciphertext);
        } catch (GeneralSecurityException e) {
            throw new IOException("Decryption failed", e);
        }
    }
}
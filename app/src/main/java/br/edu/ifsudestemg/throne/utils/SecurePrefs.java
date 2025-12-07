package br.edu.ifsudestemg.throne.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecurePrefs {

    private static final String FILE_NAME = "secure_prefs";
    private static final String KEY_API = "gemini_api_key";

    private final SharedPreferences prefs;

    public SecurePrefs(Context context) throws GeneralSecurityException, IOException {

        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        prefs = EncryptedSharedPreferences.create(
                context,
                FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public void saveApiKey(String apiKey) {
        prefs.edit().putString(KEY_API, apiKey).apply();
    }

    public String getApiKey() {
        return prefs.getString(KEY_API, null);
    }

    public void removeApiKey() {
        prefs.edit().remove(KEY_API).apply();
    }

    public boolean hasApiKey() {
        String key = getApiKey();
        return key != null && !key.isEmpty();
    }
}

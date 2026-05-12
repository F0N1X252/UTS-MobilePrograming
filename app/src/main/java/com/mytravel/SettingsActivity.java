package com.mytravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.io.File;

public class SettingsActivity extends BaseActivity {

    private TextView tvCacheSize;
    private MaterialSwitch switchDarkMode;
    private ImageView ivThemeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.menu_settings);
        }

        tvCacheSize = findViewById(R.id.tvCacheSize);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        ivThemeIcon = findViewById(R.id.ivThemeIcon);

        setupClickListeners();
        updateThemeSwitch();
        updateCacheSize();
    }

    private void setupClickListeners() {
        findViewById(R.id.cardEditProfile).setOnClickListener(v -> 
                startActivity(new Intent(this, ProfileActivity.class)));

        findViewById(R.id.cardChangeLanguage).setOnClickListener(v -> showLanguageDialog());

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences prefs = getSharedPreferences(THEME_PREFS, MODE_PRIVATE);
            boolean currentMode = prefs.getBoolean(KEY_IS_DARK_MODE, false);
            if (isChecked != currentMode) {
                toggleTheme();
            }
        });

        findViewById(R.id.cardClearCache).setOnClickListener(v -> showClearCacheConfirmation());

        findViewById(R.id.cardLogout).setOnClickListener(v -> showLogoutConfirmation());
    }

    private void updateThemeSwitch() {
        SharedPreferences prefs = getSharedPreferences(THEME_PREFS, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false);
        switchDarkMode.setChecked(isDarkMode);
        ivThemeIcon.setImageResource(isDarkMode ? R.drawable.ic_moon : R.drawable.ic_sun);
    }

    private void updateCacheSize() {
        long size = getDirSize(getCacheDir());
        tvCacheSize.setText(formatSize(size));
    }

    private long getDirSize(File dir) {
        long size = 0;
        if (dir != null && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) size += file.length();
                else if (file.isDirectory()) size += getDirSize(file);
            }
        } else if (dir != null && dir.isFile()) {
            size += dir.length();
        }
        return size;
    }

    private String formatSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new java.text.DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private void showClearCacheConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Clear Cache")
                .setMessage("Are you sure you want to clear all cached files?")
                .setPositiveButton("Clear", (dialog, which) -> clearCache())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearCache() {
        deleteDir(getCacheDir());
        updateCacheSize();
        Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show();
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) return false;
            }
        }
        return dir != null && dir.delete();
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Indonesian"};
        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_language)
                .setItems(languages, (dialog, which) -> {
                    String lang = (which == 0) ? "en" : "in";
                    LocaleHelper.setLocale(this, lang);
                    recreate();
                })
                .show();
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("No", null)
                .show();
    }

    private void logout() {
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userPrefs.edit().putBoolean("isLoggedIn", false).apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
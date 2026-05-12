package com.mytravel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity {
    protected static final String THEME_PREFS = "ThemePrefs";
    protected static final String KEY_IS_DARK_MODE = "isDarkMode";
    
    private static Bitmap sTransitionBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // We don't call applySavedTheme() here because setDefaultNightMode 
        // should be set at app startup or when changed. 
        // However, to ensure consistency on every activity start:
        applySavedTheme();
        
        super.onCreate(savedInstanceState);

        // If we have a transition bitmap, show it and fade it out
        if (sTransitionBitmap != null) {
            final ViewGroup root = (ViewGroup) getWindow().getDecorView();
            final ImageView overlay = new ImageView(this);
            overlay.setImageBitmap(sTransitionBitmap);
            overlay.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 
                    ViewGroup.LayoutParams.MATCH_PARENT));
            root.addView(overlay);
            
            sTransitionBitmap = null; // Clear for next time
            
            overlay.animate()
                    .alpha(0f)
                    .setDuration(600)
                    .withEndAction(() -> root.removeView(overlay))
                    .start();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    protected void toggleTheme() {
        SharedPreferences prefs = getSharedPreferences(THEME_PREFS, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false);
        boolean newDarkMode = !isDarkMode;
        
        prefs.edit().putBoolean(KEY_IS_DARK_MODE, newDarkMode).apply();

        // Capture current UI to mask the transition
        final ViewGroup root = (ViewGroup) getWindow().getDecorView();
        sTransitionBitmap = Bitmap.createBitmap(root.getWidth(), root.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(sTransitionBitmap);
        root.draw(canvas);

        // Update the theme mode
        AppCompatDelegate.setDefaultNightMode(newDarkMode ? 
            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Manually recreate to ensure the transition bitmap is used
        recreate();
    }

    protected void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences(THEME_PREFS, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false);
        int mode = isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        if (AppCompatDelegate.getDefaultNightMode() != mode) {
            AppCompatDelegate.setDefaultNightMode(mode);
        }
    }
}
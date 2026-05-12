package com.mytravel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ProfileActivity extends BaseActivity {

    private EditText etUsername, etName, etEmail, etPassword;
    private SharedPreferences userPrefs;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.menu_profile);
        }

        etUsername = findViewById(R.id.etProfileUsername);
        etName = findViewById(R.id.etProfileName);
        etEmail = findViewById(R.id.etProfileEmail);
        etPassword = findViewById(R.id.etProfilePassword);
        Button btnSave = findViewById(R.id.btnSaveProfile);

        userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUser = userPrefs.getString("current_user", "");

        if (!currentUser.isEmpty()) {
            etUsername.setText(currentUser);
            etName.setText(userPrefs.getString(currentUser + "_name", ""));
            etEmail.setText(userPrefs.getString(currentUser + "_email", ""));
            etPassword.setText(userPrefs.getString(currentUser, ""));
        }

        btnSave.setOnClickListener(v -> {
            String newUsername = etUsername.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (newUsername.isEmpty() || name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = userPrefs.edit();

            // If username changed, migrate data and check for availability
            if (!newUsername.equals(currentUser)) {
                if (userPrefs.contains(newUsername)) {
                    Toast.makeText(this, R.string.error_user_exists, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Remove old entries
                editor.remove(currentUser);
                editor.remove(currentUser + "_name");
                editor.remove(currentUser + "_email");
                
                // Update current user session
                editor.putString("current_user", newUsername);
            }

            // Save under new (or same) username
            editor.putString(newUsername, pass);
            editor.putString(newUsername + "_name", name);
            editor.putString(newUsername + "_email", email);
            editor.apply();

            Toast.makeText(this, R.string.success_profile_updated, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auth_menu, menu);
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        if (themeItem != null) {
            SharedPreferences prefs = getSharedPreferences(THEME_PREFS, MODE_PRIVATE);
            boolean isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false);
            themeItem.setIcon(isDarkMode ? R.drawable.ic_moon : R.drawable.ic_sun);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_theme) {
            toggleTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
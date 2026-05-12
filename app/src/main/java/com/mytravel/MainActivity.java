package com.mytravel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends BaseActivity {

    private EditText etName, etEmail, etPhone, etDate;
    private AutoCompleteTextView spinnerDestination;
    private RadioGroup rgClass;
    private CheckBox cbInsurance, cbPickup, cbPhoto, cbAgreement;
    private Button btnBook;

    private String selectedDate = "";
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyTravelPrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);

        // Navigation: Back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.book_button);
        }

        // Initialize UI components
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDate = findViewById(R.id.etDate);
        spinnerDestination = findViewById(R.id.spinnerDestination);
        rgClass = findViewById(R.id.rgClass);
        cbInsurance = findViewById(R.id.cbInsurance);
        cbPickup = findViewById(R.id.cbPickup);
        cbPhoto = findViewById(R.id.cbPhoto);
        cbAgreement = findViewById(R.id.cbAgreement);
        btnBook = findViewById(R.id.btnBook);

        // Setup Destination Dropdown
        String[] destinations = getResources().getStringArray(R.array.destinations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, destinations);
        spinnerDestination.setAdapter(adapter);

        // Check for Quick-Book extra
        String promoDest = getIntent().getStringExtra("DESTINATION_PROMO"); // Pakai key yang sama dengan Fragment
        if (promoDest != null && !promoDest.isEmpty()) {
            // Isi otomatis dropdown-nya
            spinnerDestination.setText(promoDest, false);

            // Opsional: Kasih feedback kalau promonya aktif
            Toast.makeText(this, "Promo " + promoDest + " diterapkan!", Toast.LENGTH_SHORT).show();
        }

        // SharedPreferences for "Remember Me"
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loadPreferences();

        etDate.setOnClickListener(v -> showDatePicker());

        btnBook.setOnClickListener(v -> validateAndBook());
    }

    private void loadPreferences() {
        String savedName = sharedPreferences.getString(KEY_NAME, "");
        String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");
        etName.setText(savedName);
        etEmail.setText(savedEmail);
    }

    private void savePreferences(String name, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void validateAndBook() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String destination = spinnerDestination.getText().toString();

        int selectedClassId = rgClass.getCheckedRadioButtonId();
        
        // Validation
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_name, Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_date, Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_destination, Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedClassId == -1) {
            Toast.makeText(this, R.string.error_no_class, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbAgreement.isChecked()) {
            Toast.makeText(this, R.string.error_no_agreement, Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rbSelectedClass = findViewById(selectedClassId);
        String ticketClass = rbSelectedClass.getText().toString();

        StringBuilder services = new StringBuilder();
        if (cbInsurance.isChecked()) services.append("Insurance, ");
        if (cbPickup.isChecked()) services.append("Pickup, ");
        if (cbPhoto.isChecked()) services.append("Photo, ");
        
        String servicesStr = services.length() > 0 ? services.substring(0, services.length() - 2) : "None";

        // Save preferences
        savePreferences(name, email);

        // Save Ticket to SharedPreferences
        saveTicket(destination, selectedDate, ticketClass, servicesStr);

        // Explicit Intent
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("EXTRA_NAME", name);
        intent.putExtra("EXTRA_EMAIL", email);
        intent.putExtra("EXTRA_PHONE", phone);
        intent.putExtra("EXTRA_DATE", selectedDate);
        intent.putExtra("EXTRA_DESTINATION", destination);
        intent.putExtra("EXTRA_CLASS", ticketClass);
        intent.putExtra("EXTRA_SERVICES", servicesStr);
        startActivity(intent);
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
            finish(); // Back navigation
            return true;
        } else if (itemId == R.id.action_theme) {
            toggleTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTicket(String dest, String date, String tClass, String services) {
        SharedPreferences ticketPrefs = getSharedPreferences("TicketPrefs", MODE_PRIVATE);
        String ticketsJson = ticketPrefs.getString("saved_tickets", "[]");

        try {
            JSONArray jsonArray = new JSONArray(ticketsJson);
            JSONObject newTicket = new JSONObject();
            newTicket.put("destination", dest);
            newTicket.put("date", date);
            newTicket.put("status", "PAID"); // Default status for booked ticket
            newTicket.put("class", tClass);
            newTicket.put("services", services);
            
            jsonArray.put(newTicket);
            
            ticketPrefs.edit().putString("saved_tickets", jsonArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
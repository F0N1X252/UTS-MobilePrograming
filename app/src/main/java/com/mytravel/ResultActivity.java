package com.mytravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_result);

        // Navigation: Back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String name = intent.getStringExtra("EXTRA_NAME");
        String email = intent.getStringExtra("EXTRA_EMAIL");
        String phone = intent.getStringExtra("EXTRA_PHONE");
        String date = intent.getStringExtra("EXTRA_DATE");
        String destination = intent.getStringExtra("EXTRA_DESTINATION");
        String ticketClass = intent.getStringExtra("EXTRA_CLASS");
        String services = intent.getStringExtra("EXTRA_SERVICES");

        // Pass data to Fragment
        TicketFragment fragment = new TicketFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("phone", phone);
        bundle.putString("date", date);
        bundle.putString("destination", destination);
        bundle.putString("class", ticketClass);
        bundle.putString("services", services);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        // Implicit Intents
        Button btnContact = findViewById(R.id.btnContactAgent);
        Button btnWebsite = findViewById(R.id.btnVisitWebsite);
        Button btnShare = findViewById(R.id.btnShareTicket);

        btnContact.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:08123456789"));
            startActivity(dialIntent);
        });

        btnWebsite.setOnClickListener(v -> {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setData(Uri.parse("https://www.travelkuy.com"));
            startActivity(viewIntent);
        });

        btnShare.setOnClickListener(v -> {
            String shareMessage = "My Travel Ticket:\n" +
                    "Name: " + name + "\n" +
                    "Destination: " + destination + "\n" +
                    "Class: " + ticketClass + "\n" +
                    "Date: " + date;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        Button btnDownload = findViewById(R.id.btnDownloadPdf);
        btnDownload.setOnClickListener(v -> generatePDF());
    }

    private void generatePDF() {
        View view = findViewById(R.id.fragment_container);
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(view.getWidth(), view.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        document.finishPage(page);

        File cachePath = new File(getCacheDir(), "tickets");
        cachePath.mkdirs();
        File file = new File(cachePath, "ticket_" + System.currentTimeMillis() + ".pdf");

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            document.writeTo(outputStream);
            Toast.makeText(this, "PDF Generated Successfully", Toast.LENGTH_SHORT).show();
            openPDF(file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            document.close();
        }
    }

    private void openPDF(File file) {
        Uri uri = FileProvider.getUriForFile(this, "com.mytravel.fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Open PDF with"));
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
}
package com.mytravel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_home, container, false);

        TextView tvGreeting = view.findViewById(R.id.tvGreeting);
        TextView tvTotalTickets = view.findViewById(R.id.tvTotalTickets);
        TextView tvNextDest = view.findViewById(R.id.tvNextDest);
        TextView tvNextDate = view.findViewById(R.id.tvNextDate);

        SharedPreferences userPrefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = userPrefs.getString("current_user", "");
        String name = userPrefs.getString(currentUser + "_name", "Traveler");
        tvGreeting.setText("Hello, " + name + "!");

        // Load summary data
        SharedPreferences ticketPrefs = requireContext().getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE);
        String json = ticketPrefs.getString("saved_tickets", "[]");

        try {
            JSONArray array = new JSONArray(json);
            tvTotalTickets.setText(String.valueOf(array.length()));

            if (array.length() > 0) {
                // Show the most recent booked ticket as "Next Adventure"
                JSONObject lastTicket = array.getJSONObject(array.length() - 1);
                tvNextDest.setText(lastTicket.getString("destination"));
                tvNextDate.setText(lastTicket.getString("date"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        com.google.android.material.card.MaterialCardView cardPromo1 = view.findViewById(R.id.cardPromoMudik); // Ganti ID sesuai XML-mu
        com.google.android.material.card.MaterialCardView cardPromo2 = view.findViewById(R.id.cardPromoBali);  // Ganti ID sesuai XML-mu

        if (cardPromo1 != null) {
            cardPromo1.setOnClickListener(v -> {

                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.putExtra("DESTINATION_PROMO", "Jakarta");
                startActivity(intent);
            });
        }

        if (cardPromo2 != null) {
            cardPromo2.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.putExtra("DESTINATION_PROMO", "Bali");
                startActivity(intent);
            });
        }
        return view;
    }
}
package com.mytravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyTicketsFragment extends Fragment {

    private RecyclerView rvOrdered;
    private TextView tvEmptyState;
    private TicketAdapter orderedAdapter;
    private List<Travel> orderedList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tickets, container, false);

        // Ordered Tickets List
        rvOrdered = view.findViewById(R.id.rvOrderedTickets);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        rvOrdered.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshTickets();

        return view;
    }

    private void refreshTickets() {
        loadSavedTickets();

        if (orderedList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvOrdered.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvOrdered.setVisibility(View.VISIBLE);

            TicketAdapter.OnTicketClickListener listener = new TicketAdapter.OnTicketClickListener() {
                @Override
                public void onCancel(int position, Travel ticket) {
                    showCancelConfirmation(ticket);
                }

                @Override
                public void onPay(int position, Travel ticket) {
                    payTicket(ticket);
                }

                @Override
                public void onClick(Travel ticket) {
                    showTicketDetail(ticket);
                }
            };

            orderedAdapter = new TicketAdapter(orderedList, listener);
            rvOrdered.setAdapter(orderedAdapter);
        }
    }

    private void showTicketDetail(Travel ticket) {
        SharedPreferences userPrefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = userPrefs.getString("current_user", "");
        
        Intent intent = new Intent(getContext(), ResultActivity.class);
        intent.putExtra("EXTRA_NAME", userPrefs.getString(currentUser + "_name", "User"));
        intent.putExtra("EXTRA_EMAIL", userPrefs.getString(currentUser + "_email", ""));
        intent.putExtra("EXTRA_PHONE", ""); 
        intent.putExtra("EXTRA_DATE", ticket.getDetails());
        intent.putExtra("EXTRA_DESTINATION", ticket.getDestination());
        intent.putExtra("EXTRA_CLASS", ticket.getTClass()); 
        intent.putExtra("EXTRA_SERVICES", ticket.getServices());
        startActivity(intent);
    }

    private void showCancelConfirmation(Travel ticket) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Cancel Ticket")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> cancelTicket(ticket))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void payTicket(Travel ticket) {
        SharedPreferences prefs = requireContext().getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE);
        String json = prefs.getString("saved_tickets", "[]");

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("destination").equals(ticket.getDestination()) &&
                    obj.getString("date").equals(ticket.getDetails())) {
                    obj.put("status", "PAID");
                }
            }
            prefs.edit().putString("saved_tickets", array.toString()).apply();
            Toast.makeText(getContext(), "Ticket paid successfully", Toast.LENGTH_SHORT).show();
            refreshTickets();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cancelTicket(Travel ticket) {
        SharedPreferences prefs = requireContext().getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE);
        String json = prefs.getString("saved_tickets", "[]");

        try {
            JSONArray array = new JSONArray(json);
            JSONArray newArray = new JSONArray();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                // Check if this is the ticket to cancel (using all fields as identifier)
                if (obj.getString("destination").equals(ticket.getDestination()) &&
                    obj.getString("date").equals(ticket.getDetails()) &&
                    obj.getString("status").equals(ticket.getPrice())) {
                    continue; // Skip the ticket to be cancelled
                }
                newArray.put(obj);
            }
            prefs.edit().putString("saved_tickets", newArray.toString()).apply();
            Toast.makeText(getContext(), "Ticket cancelled successfully", Toast.LENGTH_SHORT).show();
            refreshTickets();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedTickets() {
        orderedList = new ArrayList<>();
        SharedPreferences prefs = requireContext().getSharedPreferences("TicketPrefs", Context.MODE_PRIVATE);
        String json = prefs.getString("saved_tickets", "[]");

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Travel travel = new Travel(
                        obj.getString("destination"),
                        obj.getString("date"),
                        obj.getString("status"),
                        obj.optString("class", "EXECUTIVE"),
                        obj.optString("services", "None")
                );
                
                orderedList.add(travel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
package com.mytravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TicketFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        TextView tvName = view.findViewById(R.id.tvTicketName);
        TextView tvEmail = view.findViewById(R.id.tvTicketEmail);
        TextView tvPhone = view.findViewById(R.id.tvTicketPhone);
        TextView tvDestination = view.findViewById(R.id.tvTicketDestination);
        TextView tvDate = view.findViewById(R.id.tvTicketDate);
        TextView tvClass = view.findViewById(R.id.tvTicketClass);
        TextView tvServices = view.findViewById(R.id.tvTicketServices);

        if (getArguments() != null) {
            tvName.setText(getArguments().getString("name"));
            tvEmail.setText(getArguments().getString("email"));
            tvPhone.setText(getArguments().getString("phone"));
            tvDestination.setText(getArguments().getString("destination"));
            tvDate.setText(getArguments().getString("date"));
            tvClass.setText(getArguments().getString("class"));
            tvServices.setText(getArguments().getString("services"));
        }

        return view;
    }
}
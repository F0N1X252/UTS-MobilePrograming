package com.mytravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AvailableTravelFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_travel, container, false);

        RecyclerView rvAvailable = view.findViewById(R.id.rvAvailableTravel);
        rvAvailable.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Travel> travels = new ArrayList<>();
        travels.add(new Travel("Bali - Beach Tour", "Mon-Fri | 08:00 AM", "IDR 3,500,000"));
        travels.add(new Travel("Raja Ampat - Diving", "Tue-Sun | 07:00 AM", "IDR 8,200,000"));
        travels.add(new Travel("Labuan Bajo - Sailing", "Sat-Sun | 09:00 AM", "IDR 5,500,000"));
        travels.add(new Travel("Lake Toba - Cultural", "Wed-Sat | 10:00 AM", "IDR 2,800,000"));
        travels.add(new Travel("Bromo - Sunrise", "Fri-Mon | 03:00 AM", "IDR 1,500,000"));

        TravelAdapter adapter = new TravelAdapter(travels);
        rvAvailable.setAdapter(adapter);

        return view;
    }
}
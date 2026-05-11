package com.mytravel;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {

    private List<Travel> travelList;

    public TravelAdapter(List<Travel> travelList) {
        this.travelList = travelList;
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, parent, false);
        return new TravelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        Travel travel = travelList.get(position);
        holder.tvDest.setText(travel.getDestination());
        holder.tvDetails.setText(travel.getDetails());
        holder.tvPrice.setText(travel.getPrice());

        holder.btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.putExtra("EXTRA_DESTINATION", travel.getDestination());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return travelList.size();
    }

    public static class TravelViewHolder extends RecyclerView.ViewHolder {
        TextView tvDest, tvDetails, tvPrice;
        Button btnBook;
        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDest = itemView.findViewById(R.id.tvTravelDest);
            tvDetails = itemView.findViewById(R.id.tvTravelDetails);
            tvPrice = itemView.findViewById(R.id.tvTravelPrice);
            btnBook = itemView.findViewById(R.id.btnBookTravel);
        }
    }
}
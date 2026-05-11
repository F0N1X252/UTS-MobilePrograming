package com.mytravel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    public interface OnTicketClickListener {
        void onCancel(int position, Travel ticket);
        void onPay(int position, Travel ticket);
        void onClick(Travel ticket);
    }

    private List<Travel> ticketList;
    private OnTicketClickListener ticketListener;

    public TicketAdapter(List<Travel> ticketList, OnTicketClickListener listener) {
        this.ticketList = ticketList;
        this.ticketListener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Travel ticket = ticketList.get(position);
        holder.tvDest.setText(ticket.getDestination());
        holder.tvDate.setText(ticket.getDetails());
        holder.tvStatus.setText(ticket.getPrice());

        // Handle button visibility based on status
        if ("PAID".equals(ticket.getPrice())) {
            holder.btnPay.setVisibility(View.GONE);
        } else {
            holder.btnPay.setVisibility(View.VISIBLE);
        }

        holder.btnCancel.setOnClickListener(v -> {
            if (ticketListener != null) {
                ticketListener.onCancel(position, ticket);
            }
        });

        holder.btnPay.setOnClickListener(v -> {
            if (ticketListener != null) {
                ticketListener.onPay(position, ticket);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (ticketListener != null) {
                ticketListener.onClick(ticket);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvDest, tvDate, tvStatus;
        Button btnCancel, btnPay;
        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDest = itemView.findViewById(R.id.tvTicketDest);
            tvDate = itemView.findViewById(R.id.tvTicketDate);
            tvStatus = itemView.findViewById(R.id.tvTicketStatus);
            btnCancel = itemView.findViewById(R.id.btnCancelTicket);
            btnPay = itemView.findViewById(R.id.btnPayTicket);
        }
    }
}
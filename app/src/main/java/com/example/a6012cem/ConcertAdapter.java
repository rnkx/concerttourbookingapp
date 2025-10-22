package com.example.a6012cem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ConcertAdapter extends RecyclerView.Adapter<ConcertAdapter.ConcertViewHolder> {

    public interface OnConcertClickListener {
        void onEditClick(Concert concert);
        void onDeleteClick(Concert concert);
        void onBookClick(Concert concert);

        void onConcertClick(Concert concert);
    }

    public List<Concert> concertList; // Make public for direct access
    private Context context;
    private OnConcertClickListener listener;
    private boolean isAdminMode = false;

    public ConcertAdapter(List<Concert> concertList, Context context, OnConcertClickListener listener) {
        this.concertList = concertList;
        this.context = context;
        this.listener = listener;
    }

    // Add this method if it doesn't exist
    public void setAdminMode(boolean adminMode) {
        this.isAdminMode = adminMode;
        notifyDataSetChanged();
    }

    // Add this method if it doesn't exist
    public void updateConcerts(List<Concert> newConcertList) {
        this.concertList = newConcertList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConcertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_concert, parent, false);
        return new ConcertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertViewHolder holder, int position) {
        Concert concert = concertList.get(position);

        holder.tvTitle.setText(concert.getTitle());
        holder.tvArtist.setText(concert.getArtist());
        holder.tvVenue.setText(concert.getVenue());
        holder.tvDateTime.setText(concert.getDateTime());
        holder.tvPrice.setText(String.format("RM %.2f", concert.getPrice()));
        holder.tvTickets.setText("Tickets: " + concert.getTicketsAvailable());

        try {
            holder.imgConcert.setImageResource(concert.getImageResId());
        } catch (Exception e) {
            holder.imgConcert.setImageResource(R.drawable.ic_menu_gallery);
        }

        // DEBUG: Check if admin mode is working
        System.out.println("Admin mode: " + isAdminMode);

        if (isAdminMode) {
            // Show EDIT and DELETE buttons, hide BOOK button
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnBook.setVisibility(View.GONE);

            holder.btnEdit.setOnClickListener(v -> {
                System.out.println("Edit clicked for: " + concert.getTitle());
                listener.onEditClick(concert);
            });

            holder.btnDelete.setOnClickListener(v -> {
                System.out.println("Delete clicked for: " + concert.getTitle());
                listener.onDeleteClick(concert);
            });
        } else {
            // Show BOOK button, hide EDIT and DELETE buttons
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnBook.setVisibility(View.VISIBLE);

            holder.btnBook.setOnClickListener(v -> {
                System.out.println("Book clicked for: " + concert.getTitle());
                listener.onBookClick(concert);
            });
        }
    }

    @Override
    public int getItemCount() {
        return concertList.size();
    }

    public static class ConcertViewHolder extends RecyclerView.ViewHolder {
        ImageView imgConcert;
        TextView tvTitle, tvArtist, tvVenue, tvDateTime, tvPrice, tvTickets;
        Button btnEdit, btnDelete, btnBook;

        public ConcertViewHolder(@NonNull View itemView) {
            super(itemView);
            imgConcert = itemView.findViewById(R.id.imgConcert);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvVenue = itemView.findViewById(R.id.tvVenue);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTickets = itemView.findViewById(R.id.tvTickets);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}
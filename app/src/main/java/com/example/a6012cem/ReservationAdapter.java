package com.example.a6012cem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private List<Booking> reservationList;
    private OnReservationClickListener listener;

    public interface OnReservationClickListener {
        void onCancelReservation(Booking booking);
        void onConfirmReservation(Booking booking);
        void onBackToHome();
    }

    public ReservationAdapter(List<Booking> reservationList, OnReservationClickListener listener) {
        this.reservationList = reservationList != null ? reservationList : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false); // Make sure this matches your XML file name
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (reservationList == null || position >= reservationList.size()) {
            return;
        }

        Booking booking = reservationList.get(position);

        // Bind data with null checks
        if (holder.tvConcertTitle != null) {
            holder.tvConcertTitle.setText(booking.getConcertTitle());
        }
        if (holder.tvArtist != null) {
            holder.tvArtist.setText(booking.getArtist());
        }
        if (holder.tvTickets != null) {
            holder.tvTickets.setText(String.valueOf(booking.getNumberOfTickets()));
        }
        if (holder.tvTotalPrice != null) {
            holder.tvTotalPrice.setText(String.format("RM %.2f", booking.getTotalPrice()));
        }
        if (holder.tvBookingDate != null) {
            holder.tvBookingDate.setText(booking.getBookingDate());
        }
        if (holder.tvStatus != null) {
            holder.tvStatus.setText(booking.getStatus());
        }

        // Set click listeners with null checks
        if (holder.btnCancelBooking != null && listener != null) {
            holder.btnCancelBooking.setOnClickListener(v -> {
                listener.onCancelReservation(booking);
            });
        }
        // Back to Home button click listener
        if (holder.btnBackToHome != null && listener != null) {
            holder.btnBackToHome.setOnClickListener(v -> {
                listener.onBackToHome(); // This will be the same for all items
            });
        }

        // If you have a confirm button, add it here
        // if (holder.btnConfirmBooking != null && listener != null) {
        //     holder.btnConfirmBooking.setOnClickListener(v -> {
        //         listener.onConfirmReservation(booking);
        //     });
        // }
    }

    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }

    public void updateReservations(List<Booking> newReservations) {
        this.reservationList = newReservations != null ? newReservations : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgConcert;
        TextView tvConcertTitle, tvArtist, tvTickets, tvTotalPrice, tvBookingDate, tvStatus;
        Button btnCancelBooking;
        Button btnBackToHome;
        // Add other buttons if you have them: Button btnConfirmBooking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize all views with proper error handling
            try {
                imgConcert = itemView.findViewById(R.id.imgConcert);
                tvConcertTitle = itemView.findViewById(R.id.tvConcertTitle);
                tvArtist = itemView.findViewById(R.id.tvArtist);
                tvTickets = itemView.findViewById(R.id.tvTickets);
                tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
                tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                btnCancelBooking = itemView.findViewById(R.id.btnCancelBooking);
                btnBackToHome = itemView.findViewById(R.id.btnBackToHome); // Add this line

                // If you have a confirm button, add it here:
                // btnConfirmBooking = itemView.findViewById(R.id.btnConfirmBooking);
            } catch (Exception e) {
                // Log the error or handle it appropriately
                e.printStackTrace();
            }
        }
    }
}
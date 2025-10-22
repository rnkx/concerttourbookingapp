package com.example.a6012cem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private OnBookingClickListener listener;

    public interface OnBookingClickListener {
        void onCancelBooking(Booking booking);
    }

    public BookingAdapter(List<Booking> bookingList, OnBookingClickListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvConcertTitle.setText(booking.getConcertTitle());
        holder.tvArtist.setText(booking.getArtist());
        holder.tvTickets.setText(String.valueOf(booking.getNumberOfTickets()));
        holder.tvTotalPrice.setText(String.format("RM %.2f", booking.getTotalPrice()));
        holder.tvBookingDate.setText(booking.getBookingDate());
        holder.tvStatus.setText(booking.getStatus());

        holder.btnCancelBooking.setOnClickListener(v -> {
            listener.onCancelBooking(booking);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateBookings(List<Booking> newBookings) {
        this.bookingList = newBookings;
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvConcertTitle, tvArtist, tvTickets, tvTotalPrice, tvBookingDate, tvStatus;
        Button btnCancelBooking;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConcertTitle = itemView.findViewById(R.id.tvConcertTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvTickets = itemView.findViewById(R.id.tvTickets);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCancelBooking = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}

package com.example.a6012cem;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookingHelper {
    private static final String PREF_NAME = "BookingData";
    private static final String BOOKINGS_KEY = "bookings";
    private static final Gson gson = new Gson();

    // Save a booking
    public static void saveBooking(Context context, Booking booking) {
        List<Booking> bookings = getAllBookings(context);
        bookings.add(booking);
        saveAllBookings(context, bookings);
    }

    // Get all bookings (for admin)
    public static List<Booking> getAllBookings(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String bookingsJson = prefs.getString(BOOKINGS_KEY, "[]");

        Type listType = new TypeToken<List<Booking>>(){}.getType();
        List<Booking> bookings = gson.fromJson(bookingsJson, listType);

        return bookings != null ? bookings : new ArrayList<>();
    }
    // Get bookings for specific user (for customer)
    public static List<Booking> getUserBookings(Context context, String userId) {
        List<Booking> allBookings = getAllBookings(context);
        List<Booking> userBookings = new ArrayList<>();

        for (Booking booking : allBookings) {
            if (booking.getUserId().equals(userId)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    // Delete a booking
    public static void deleteBooking(Context context, String bookingId) {
        List<Booking> bookings = getAllBookings(context);

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId().equals(bookingId)) {
                bookings.remove(i);
                break;
            }
        }

        saveAllBookings(context, bookings);
    }

    // Update booking status (for admin)
    public static void updateBookingStatus(Context context, String bookingId, String newStatus) {
        List<Booking> bookings = getAllBookings(context);

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId().equals(bookingId)) {
                bookings.get(i).setStatus(newStatus);
                break;
            }
        }

        saveAllBookings(context, bookings);
    }
    private static void saveAllBookings(Context context, List<Booking> bookings) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String bookingsJson = gson.toJson(bookings);
        editor.putString(BOOKINGS_KEY, bookingsJson);
        editor.apply();
    }
}
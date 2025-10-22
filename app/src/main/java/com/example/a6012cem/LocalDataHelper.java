package com.example.a6012cem;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalDataHelper {
    private static final String PREF_NAME = "ConcertData";
    private static final String CONCERTS_KEY = "concerts";
    private static final Gson gson = new Gson();

    // Get all concerts
    public List<Concert> getAllConcerts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String concertsJson = prefs.getString(CONCERTS_KEY, "[]");

        Type listType = new TypeToken<List<Concert>>(){}.getType();
        List<Concert> concerts = gson.fromJson(concertsJson, listType);

        if (concerts == null) {
            concerts = new ArrayList<>();
        }

        // Filter out inactive concerts if needed
        List<Concert> activeConcerts = new ArrayList<>();
        for (Concert concert : concerts) {
            if (concert.isActive()) {
                activeConcerts.add(concert);
            }
        }

        return activeConcerts;
    }

    // Add a new concert
    public void addConcert(Context context, Concert concert) {
        List<Concert> concerts = getAllConcerts(context);
        concerts.add(concert);
        saveConcerts(context, concerts);
    }

    // Update an existing concert
    public static void updateConcert(Context context, Concert updatedConcert) {
        List<Concert> concerts = new LocalDataHelper().getAllConcerts(context);

        for (int i = 0; i < concerts.size(); i++) {
            Concert concert = concerts.get(i);
            if (concert.getId().equals(updatedConcert.getId())) {
                concerts.set(i, updatedConcert);
                break;
            }
        }

        new LocalDataHelper().saveConcerts(context, concerts);
    }

    // Delete a concert
    public void deleteConcert(Context context, String concertId) {
        List<Concert> concerts = getAllConcerts(context);

        for (int i = 0; i < concerts.size(); i++) {
            Concert concert = concerts.get(i);
            if (concert.getId().equals(concertId)) {
                // Mark as inactive instead of removing
                concert.setActive(false);
                concerts.set(i, concert);
                break;
            }
        }

        saveConcerts(context, concerts);
    }

    // Load concerts (static method for compatibility)
    public static List<Concert> loadConcerts(Context context) {
        return new LocalDataHelper().getAllConcerts(context);
    }

    // Save concerts to SharedPreferences
    public static void saveConcerts(Context context, List<Concert> concerts) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String concertsJson = gson.toJson(concerts);
        editor.putString(CONCERTS_KEY, concertsJson);
        editor.apply();
    }

    // Clear all data (for testing)
    public void clearAllData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(CONCERTS_KEY);
        editor.apply();
    }

    // Get concert by ID
    public Concert getConcertById(Context context, String concertId) {
        List<Concert> concerts = getAllConcerts(context);

        for (Concert concert : concerts) {
            if (concert.getId().equals(concertId)) {
                return concert;
            }
        }
        return null;
    }

    // Update tickets available
    public void updateTickets(Context context, String concertId, int newTicketCount) {
        Concert concert = getConcertById(context, concertId);
        if (concert != null) {
            concert.setTicketsAvailable(newTicketCount);
            updateConcert(context, concert);
        }
    }
}
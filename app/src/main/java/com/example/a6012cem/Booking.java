package com.example.a6012cem;

import java.util.UUID;

public class Booking {
    private String id;
    private String concertId;
    private String concertTitle;
    private String artist;
    private String bookingDate;
    private int numberOfTickets;
    private double totalPrice;
    private String status; // "Confirmed", "Cancelled", etc.

    private String userId; // Track which user made the booking
    private String userName; // User's name for admin view

    public Booking(String concertId, String concertTitle, String artist, int numberOfTickets, double totalPrice, String userId, String userName) {
        this.id = UUID.randomUUID().toString();
        this.concertId = concertId;
        this.concertTitle = concertTitle;
        this.artist = artist;
        this.bookingDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
        this.numberOfTickets = numberOfTickets;
        this.totalPrice = totalPrice;
        this.status = "Confirmed";
        this.userId = userId;
        this.userName = userName;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getConcertId() { return concertId; }
    public String getConcertTitle() { return concertTitle; }
    public String getArtist() { return artist; }
    public String getBookingDate() { return bookingDate; }
    public int getNumberOfTickets() { return numberOfTickets; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
}
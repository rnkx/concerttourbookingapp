package com.example.a6012cem;

import java.io.Serializable;

public class Reservation implements Serializable {

    private String id;
    private String concertTitle;
    private String userName;
    private String userEmail;
    private int ticketCount;
    private double totalPrice;
    private String status;
    private long bookingDate;       // Stored as timestamp in Firebase
    private String concertDate;
    private String venue;

    // ✅ Required empty constructor for Firebase
    public Reservation() {}

    // 🔹 Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getConcertTitle() { return concertTitle; }
    public void setConcertTitle(String concertTitle) { this.concertTitle = concertTitle; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public int getTicketCount() { return ticketCount; }
    public void setTicketCount(int ticketCount) { this.ticketCount = ticketCount; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getBookingDate() { return bookingDate; }
    public void setBookingDate(long bookingDate) { this.bookingDate = bookingDate; }

    public String getConcertDate() { return concertDate; }
    public void setConcertDate(String concertDate) { this.concertDate = concertDate; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    // 🔹 Optional alias getters for older code compatibility
    public String getConcertName() { return concertTitle; }
    public int getTickets() { return ticketCount; }
    public String getDate() { return concertDate; }
}


package com.example.a6012cem;

public class Concert {
    private String id;
    private String title;
    private String artist;
    private String venue;
    private String dateTime;
    private double price;
    private int ticketsAvailable;
    private int imageResId;
    private boolean active;

    public Concert(String id, String title, String artist, String venue, String dateTime,
                   double price, int ticketsAvailable, int imageResId, boolean active) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.venue = venue;
        this.dateTime = dateTime;
        this.price = price;
        this.ticketsAvailable = ticketsAvailable;
        this.imageResId = imageResId;
        this.active = active;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getTicketsAvailable() { return ticketsAvailable; }
    public void setTicketsAvailable(int ticketsAvailable) { this.ticketsAvailable = ticketsAvailable; }
    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
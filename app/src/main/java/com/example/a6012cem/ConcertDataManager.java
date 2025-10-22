package com.example.a6012cem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConcertDataManager {
    private static final List<Concert> concerts = new ArrayList<>();

    static {
        // sample data
        concerts.add(new Concert(UUID.randomUUID().toString(),
                "Coldplay Live in KL", "Coldplay", "Bukit Jalil Stadium", "2025-11-10 | 20:00",
                450.00, 300, R.drawable.applogo, false));
        concerts.add(new Concert(UUID.randomUUID().toString(),
                "Taylor Swift Eras Tour", "Taylor Swift", "Axiata Arena", "2025-09-05 | 19:30",
                550.00, 500, R.drawable.applogo, false));
    }

    public static List<Concert> getConcerts() { return concerts; }

    public static void addConcert(Concert c) { concerts.add(c); }

    public static void updateConcert(Concert updated) {
        for (int i = 0; i < concerts.size(); i++) {
            if (concerts.get(i).getId().equals(updated.getId())) {
                concerts.set(i, updated);
                return;
            }
        }
    }

    public static void deleteConcert(String id) {
        concerts.removeIf(c -> c.getId().equals(id));
    }

    public static Concert findById(String id) {
        for (Concert c : concerts) if (c.getId().equals(id)) return c;
        return null;
    }
}

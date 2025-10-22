package com.example.a6012cem;

import java.util.ArrayList;
import java.util.List;

public class ConcertRepository {
    private static final List<Concert> concertList = new ArrayList<>();

    public static List<Concert> getConcertList() {
        return concertList;
    }

    public static void addConcert(Concert concert) {
        concertList.add(concert);
    }

    public static void updateConcert(Concert updatedConcert) {
        for (int i = 0; i < concertList.size(); i++) {
            if (concertList.get(i).getId().equals(updatedConcert.getId())) {
                concertList.set(i, updatedConcert);
                break;
            }
        }
    }

    public static void deleteConcert(String concertId) {
        concertList.removeIf(c -> c.getId().equals(concertId));
    }
}

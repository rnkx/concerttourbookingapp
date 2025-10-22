package com.example.a6012cem;

import android.content.Context;
import java.util.UUID;

public class DemoDataHelper {
    public static void addDemoConcerts(Context context) {
        LocalDataHelper helper = new LocalDataHelper();

        // Check if concerts already exist
        if (!helper.getAllConcerts(context).isEmpty()) return;

        helper.addConcert(context, new Concert(
                UUID.randomUUID().toString(),
                "The Eras Tour",
                "Taylor Swift",
                "Singapore National Stadium",
                "2025-03-08 | 18:00",
                65.00,
                120000,
                R.drawable.ic_menu_gallery,
                true
        ));

        helper.addConcert(context, new Concert(
                UUID.randomUUID().toString(),
                "Music of the Spheres",
                "Coldplay",
                "Bukit Jalil National Stadium",
                "2025-11-23 | 20:00",
                50.00,
                100000,
                R.drawable.ic_menu_gallery,
                true
        ));

        helper.addConcert(context, new Concert(
                UUID.randomUUID().toString(),
                "Seiko Matsuda Tour",
                "Seiko Matsuda",
                "Tokyo Dome City",
                "2025-12-24 | 19:00",
                90.00,
                80000,
                R.drawable.ic_menu_gallery,
                true
        ));
    }
}
package com.example.familyplanner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public interface Slot {
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat dateParser = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    void updateSlot(String date, String time, String plan_id) throws ParseException;

    String getDate();

    void setDate(String date);

    String getDescription();

    void setDescription(String description);

    boolean isCompletion();

    void setCompletion(boolean completion);

    boolean isMissing();

    void setMissing(boolean missing);
}

package utils;

import models.TimeSlot;

public class TimeSlotUtil {

    public static int convertTimeToMinutes(String time) {
        String[] timing = time.split(":");
        int hour = Integer.parseInt(timing[0]);
        int minutes = Integer.parseInt(timing[1]);
        return hour * 60 + minutes;
    }

    public static boolean validate(TimeSlot timeSlot) {
        int startHour = Integer.parseInt(timeSlot.getStartTime().split(":")[0]);

        if (startHour < 9 || startHour >= 21) return false;

        int totalStartMinutes = convertTimeToMinutes(timeSlot.getStartTime());
        int totalEndMinutes = convertTimeToMinutes(timeSlot.getEndTime());

        return totalEndMinutes - totalStartMinutes == 60;
    }
}
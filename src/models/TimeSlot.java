package models;

import java.util.Objects;

public class TimeSlot {
    private final String startTime;
    private final String endTime;

    public TimeSlot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeSlot other = (TimeSlot) obj;
        return Objects.equals(startTime, other.startTime) && Objects.equals(endTime, other.endTime);
    }

    @Override
    public String toString() {
        return "(" + startTime + "-" + endTime + ")";
    }
}
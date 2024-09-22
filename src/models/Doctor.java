package models;

import enums.Specialization;
import exceptions.TimeSlotNotFound;

import java.util.*;

public class Doctor extends Person {
    private final Specialization specialization;
    private final Map<TimeSlot, Boolean> availability;

    public Doctor(String name, Specialization specialization) {
        this.name = name;
        this.specialization = specialization;
        this.availability = new HashMap<>();
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void addAvailability(TimeSlot timeSlot) {
        setAvailability(timeSlot, true);
    }

    public void setAvailability(TimeSlot timeSlot, boolean availabilityStatus) {
        availability.put(timeSlot, availabilityStatus);
    }

    public Map<TimeSlot, Boolean> getAvailability() {
        return availability;
    }

    public boolean isTimeSlotAvailable(TimeSlot timeSlot) throws TimeSlotNotFound {
        if (availability.get(timeSlot) == null) {
            throw new TimeSlotNotFound("Dr." + name + " has no slot for " + timeSlot);
        }

        return availability.get(timeSlot);
    }
}
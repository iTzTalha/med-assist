package exceptions;

public class TimeSlotNotFound extends Exception {
    public TimeSlotNotFound() {
    }

    public TimeSlotNotFound(String message) {
        super(message);
    }
}

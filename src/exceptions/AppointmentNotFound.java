package exceptions;

public class AppointmentNotFound extends Exception {
    public AppointmentNotFound() {
    }

    public AppointmentNotFound(String message) {
        super(message);
    }
}

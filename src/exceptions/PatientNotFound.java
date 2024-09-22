package exceptions;

public class PatientNotFound extends Exception {
    public PatientNotFound() {
    }

    public PatientNotFound(String message) {
        super(message);
    }
}

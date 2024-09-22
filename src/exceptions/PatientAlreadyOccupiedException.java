package exceptions;

public class PatientAlreadyOccupiedException extends Exception {
    public PatientAlreadyOccupiedException() {
    }

    public PatientAlreadyOccupiedException(String message) {
        super(message);
    }
}

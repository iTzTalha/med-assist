package exceptions;

public class PatientAlreadyExists extends Exception {
    public PatientAlreadyExists() {
    }

    public PatientAlreadyExists(String message) {
        super(message);
    }
}

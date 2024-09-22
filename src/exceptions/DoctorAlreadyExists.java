package exceptions;

public class DoctorAlreadyExists extends Exception {
    public DoctorAlreadyExists() {
    }

    public DoctorAlreadyExists(String message) {
        super(message);
    }
}

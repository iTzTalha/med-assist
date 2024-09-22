package services;

import exceptions.PatientAlreadyExists;
import models.Patient;
import repositories.PatientRepository;

import java.util.Optional;

public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public boolean isRegistered(Patient patient) {
        Optional<Patient> patientOptional = patientRepository.findByName(patient.getName());
        return patientOptional.isPresent();
    }

    public Patient register(Patient patient) throws PatientAlreadyExists {
        if (isRegistered(patient)) {
            throw new PatientAlreadyExists(patient.getName() + " is already registered!");
        }

        return patientRepository.save(patient);
    }
}
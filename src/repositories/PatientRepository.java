package repositories;

import models.Patient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PatientRepository {
    private static Long ID = 1L;
    private static final Map<Long, Patient> patientDB = new HashMap<>();

    public Optional<Patient> findById(Long id) {
        return Optional.ofNullable(patientDB.get(id));
    }

    public Optional<Patient> findByName(String name) {
        return patientDB.values().stream().filter(d -> d.getName().equals(name)).findFirst();
    }

    public Patient save(Patient patient) {
        patient.setId(ID++);
        patientDB.put(patient.getId(), patient);
        return patient;
    }
}

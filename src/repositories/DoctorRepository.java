package repositories;

import enums.Specialization;
import models.Doctor;

import java.util.*;
import java.util.stream.Collectors;

public class DoctorRepository {
    private static Long ID = 1L;
    private static final Map<Long, Doctor> docDB = new HashMap<>();

    public Optional<Doctor> findById(Long id) {
        return Optional.ofNullable(docDB.get(id));
    }

    public Optional<Doctor> findByName(String name) {
        return docDB.values().stream().filter(d -> d.getName().equals(name)).findFirst();
    }

    public Doctor save(Doctor doctor) {
        doctor.setId(ID++);
        docDB.put(doctor.getId(), doctor);
        return doctor;
    }

    public List<Doctor> findBySpecialization(Specialization specialization) {
        return docDB.values().stream().filter(doctor -> doctor.getSpecialization().equals(specialization)).collect(Collectors.toList());
    }
}

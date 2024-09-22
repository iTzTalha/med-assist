package repositories;

import models.Appointment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AppointmentRepository {
    private static Long ID = 1L;
    private static final Map<Long, Appointment> appointmentDB = new HashMap<>();

    public Optional<Appointment> findById(Long id) {
        return Optional.ofNullable(appointmentDB.get(id));
    }

    public Appointment save(Appointment appointment) {
        appointment.setId(ID++);
        appointmentDB.put(appointment.getId(), appointment);
        return appointment;
    }

    public List<Appointment> findAppointments(Predicate<Appointment> predicate) {
        return appointmentDB.values().stream().filter(predicate).collect(Collectors.toList());
    }
}
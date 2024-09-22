package services;

import enums.AppointmentStatus;
import exceptions.*;
import extras.logger.ConsoleLogger;
import models.Appointment;
import models.Doctor;
import models.Patient;
import models.TimeSlot;
import repositories.AppointmentRepository;
import utils.TimeSlotUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class AppointmentService {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentRepository appointmentRepository;
    private final Deque<Appointment> waitList = new ArrayDeque<>();

    public AppointmentService(PatientService patientService, DoctorService doctorService, AppointmentRepository appointmentRepository) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment bookAppointment(Patient patient, Doctor doctor, String startTime) throws PatientNotFound, DoctorNotFoundException, PatientAlreadyOccupiedException {
        if (!patientService.isRegistered(patient)) {
            throw new PatientNotFound(patient.getName() + " doesn't exist");
        }

        if (!doctorService.isRegistered(doctor)) {
            throw new DoctorNotFoundException(doctor.getName() + " doesn't exist");
        }

        int endTimeInMinutes = TimeSlotUtil.convertTimeToMinutes(startTime) + 60;
        String endTime = endTimeInMinutes / 60 + ":" + endTimeInMinutes % 60;
        TimeSlot timeSlot = new TimeSlot(startTime, endTime);

        if (appointmentRepository.findAppointments(appointment -> appointment.getPatient().getId().equals(patient.getId()))
                .stream().anyMatch(appointment -> appointment.getTimeSlot().equals(timeSlot))) {
            throw new PatientAlreadyOccupiedException();
        }

        Appointment appointment = new Appointment(patient, doctor, timeSlot);
        try {
            if (doctor.isTimeSlotAvailable(timeSlot)) {
                appointment.setStatus(AppointmentStatus.PENDING);
                waitList.offer(appointment);
            } else {
                appointment.setStatus(AppointmentStatus.BOOKED);
                doctor.setAvailability(timeSlot, false);
            }

            return appointmentRepository.save(appointment);
        } catch (TimeSlotNotFound e) {
            new ConsoleLogger().log(e.getMessage());
            return null;
        }
    }

    public Appointment cancelAppointment(Long appointmentId) throws AppointmentNotFound {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isEmpty()) {
            throw new AppointmentNotFound("Appointment not found for id " + "'" + appointmentId + "'");
        }

        Appointment appointment = appointmentOptional.get();
        appointment.setStatus(AppointmentStatus.CANCELED);

        appointment.getDoctor().setAvailability(appointment.getTimeSlot(), true);

        return appointment;
    }
}

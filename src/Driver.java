import enums.Specialization;
import exceptions.*;
import extras.logger.ConsoleLogger;
import extras.logger.Logger;
import models.Appointment;
import models.Doctor;
import models.Patient;
import models.TimeSlot;
import repositories.AppointmentRepository;
import repositories.DoctorRepository;
import repositories.PatientRepository;
import services.AppointmentService;
import services.DoctorService;
import services.PatientService;

import java.util.Map;
import java.util.Set;

public class Driver {
    public static void main(String[] args) {
        final Logger logger = new ConsoleLogger();

        final DoctorRepository doctorRepository = new DoctorRepository();
        final DoctorService doctorService = new DoctorService(doctorRepository);

        final PatientRepository patientRepository = new PatientRepository();
        final PatientService patientService = new PatientService(patientRepository);

        final AppointmentRepository appointmentRepository = new AppointmentRepository();
        final AppointmentService appointmentService = new AppointmentService(patientService, doctorService, appointmentRepository);

        // Doctors
        Doctor doctorCurious = new Doctor("curious", Specialization.CARDIOLOGIST);
        Doctor doctorDreadful = new Doctor("Dreadful", Specialization.DERMATOLOGIST);
        Doctor doctorDaring = new Doctor("Daring", Specialization.DERMATOLOGIST);

        // Patients
        Patient patientA = new Patient("PatientA");
        Patient patientB = new Patient("PatientB");
        Patient patientC = new Patient("PatientC");

        // Registering doctor
        try {
            doctorCurious = doctorService.register(doctorCurious);
            logger.log("Welcome Dr. " + doctorCurious.getName() + " !!");
        } catch (DoctorAlreadyExists e) {
            logger.log(e.getMessage());
        }

        try {
            doctorDreadful = doctorService.register(doctorDreadful);
            logger.log("Welcome Dr. " + doctorDreadful.getName() + " !!");
        } catch (DoctorAlreadyExists e) {
            logger.log(e.getMessage());
        }

        // Add invalid Availability
        try {
            doctorService.addAvailability(doctorCurious.getId(), new TimeSlot("9:30", "10:00"));
        } catch (DoctorNotFoundException e) {
            logger.log(e.getMessage());
        }
        // Add Valid Availabilities 9:30-10:00, 12:30-13:00, 16:00-16:30
        try {
            doctorService.addAvailability(doctorCurious.getId(), new TimeSlot("9:30", "10:30"), new TimeSlot("12:30", "13:30"), new TimeSlot("16:00", "17:00"));
            logger.log("Done Doc!");
        } catch (DoctorNotFoundException e) {
            logger.log(e.getMessage());
        }
        try {
            doctorService.addAvailability(doctorDreadful.getId(), new TimeSlot("12:30", "13:30"), new TimeSlot("13:07", "14:07"));
            logger.log("Done Doc!");
        } catch (DoctorNotFoundException e) {
            logger.log(e.getMessage());
        }

        Map<Doctor, Set<TimeSlot>> availableCardiologists = doctorService.showAvailability(Specialization.CARDIOLOGIST);
        availableCardiologists.forEach((key, value) -> System.out.println(key.getName() + ": " + value));

        try {
            patientA = patientService.register(patientA);
            logger.log(patientA.getName() + " registered successfully");
        } catch (PatientAlreadyExists e) {
            logger.log(e.getMessage());
        }
        try {
            patientB = patientService.register(patientB);
            logger.log(patientB.getName() + " registered successfully");
        } catch (PatientAlreadyExists e) {
            logger.log(e.getMessage());
        }
        try {
            patientC = patientService.register(patientC);
            logger.log(patientC.getName() + " registered successfully");
        } catch (PatientAlreadyExists e) {
            logger.log(e.getMessage());
        }

        Appointment patientA_Curious_Appointment = null, patientB_Curious_Appointment = null, patientC_Curious_Appointment = null, patientC_Dreadful_Appointment = null;
        try {
            patientA_Curious_Appointment = appointmentService.bookAppointment(patientA, doctorCurious, "12:30");
            if (patientA_Curious_Appointment != null) {
                logger.log("Booked. Booking id: " + patientA_Curious_Appointment.getId());
            }
        } catch (PatientNotFound | PatientAlreadyOccupiedException | DoctorNotFoundException e) {
            logger.log(e.getMessage());
        }

        availableCardiologists = doctorService.showAvailability(Specialization.CARDIOLOGIST);
        availableCardiologists.forEach((key, value) -> System.out.println(key.getName() + ": " + value));

        try {
            patientB_Curious_Appointment = appointmentService.bookAppointment(patientB, doctorCurious, "12:30");
            if (patientB_Curious_Appointment != null) {
                logger.log("Booked. Booking id: " + patientB_Curious_Appointment.getId());
            }
        } catch (PatientNotFound | DoctorNotFoundException | PatientAlreadyOccupiedException e) {
            logger.log(e.getMessage());
        }
        try {
            patientC_Curious_Appointment = appointmentService.bookAppointment(patientC, doctorCurious, "12:30");
            if (patientC_Curious_Appointment != null) {
                logger.log("Booked. Booking id: " + patientC_Curious_Appointment.getId());
            }
        } catch (PatientNotFound | DoctorNotFoundException | PatientAlreadyOccupiedException e) {
            logger.log(e.getMessage());
        }

        if (patientA_Curious_Appointment != null) {
            try {
                appointmentService.cancelAppointment(patientA_Curious_Appointment.getId());
                logger.log("cancelBookingId: " + patientA_Curious_Appointment.getId());
            } catch (AppointmentNotFound e) {
                logger.log(e.getMessage());
            }
        }

        availableCardiologists = doctorService.showAvailability(Specialization.CARDIOLOGIST);
        availableCardiologists.forEach((key, value) -> System.out.println(key.getName() + ": " + value));

        if (patientB_Curious_Appointment != null) {
            try {
                appointmentService.cancelAppointment(patientB_Curious_Appointment.getId());
                logger.log("cancelBookingId: " + patientB_Curious_Appointment.getId());
            } catch (AppointmentNotFound e) {
                logger.log(e.getMessage());
            }
        }

        try {
            patientC_Dreadful_Appointment = appointmentService.bookAppointment(patientC, doctorDreadful, "13:07");
            if (patientC_Dreadful_Appointment != null) {
                logger.log("Booked. Booking id: " + patientC_Dreadful_Appointment.getId());
            }
        } catch (PatientNotFound | DoctorNotFoundException | PatientAlreadyOccupiedException e) {
            logger.log(e.getMessage());
        }

        try {
            Appointment appointment = appointmentService.bookAppointment(patientC,doctorCurious,"13:07");
            if (appointment != null) {
                logger.log("Booked. Booking id: " + appointment.getId());
            }
        } catch (PatientNotFound | DoctorNotFoundException | PatientAlreadyOccupiedException e) {
            logger.log(e.getMessage());
        }

        Map<Doctor, Set<TimeSlot>> availableDermatologist = doctorService.showAvailability(Specialization.DERMATOLOGIST);
        availableDermatologist.forEach((key, value) -> System.out.println(key.getName() + ": " + value));

        if (patientA_Curious_Appointment != null) {
            try {
                appointmentService.cancelAppointment(patientA_Curious_Appointment.getId());
                logger.log("cancelBookingId: " + patientA_Curious_Appointment.getId());
            } catch (AppointmentNotFound e) {
                logger.log(e.getMessage());
            }
        }
    }
}

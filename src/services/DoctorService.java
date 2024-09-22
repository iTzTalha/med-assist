package services;

import enums.Specialization;
import exceptions.DoctorAlreadyExists;
import exceptions.DoctorNotFoundException;
import extras.logger.ConsoleLogger;
import extras.logger.Logger;
import models.Doctor;
import models.TimeSlot;
import repositories.DoctorRepository;
import utils.TimeSlotUtil;

import java.util.*;
import java.util.stream.Collectors;

public class DoctorService {
    private static final Logger logger = new ConsoleLogger();

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public boolean isRegistered(Doctor doctor) {
        Optional<Doctor> doctorOptional = doctorRepository.findByName(doctor.getName());
        return doctorOptional.isPresent();
    }

    public Doctor register(Doctor doctor) throws DoctorAlreadyExists {
        if (isRegistered(doctor)) {
            throw new DoctorAlreadyExists(doctor.getName() + " is already registered!");
        }

        return doctorRepository.save(doctor);
    }

    public void addAvailability(Long id, TimeSlot... timeSlots) throws DoctorNotFoundException {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isEmpty()) {
            throw new DoctorNotFoundException("No doctor found for id '" + id + "'");
        }

        Doctor doctor = doctorOptional.get();

        Arrays.stream(timeSlots).forEach(timeSlot -> {
            if (!TimeSlotUtil.validate(timeSlot)) {
                logger.log("Sorry Dr. " + doctor.getName() + ", " + timeSlot + " slot must be 60 mins only and between 9am - 9pm");
            } else {
                boolean flag = false;
                for (TimeSlot it : doctor.getAvailability().keySet()) {
                    if (it.equals(timeSlot)) {
                        logger.log("Sorry Dr. " + doctor.getName() + ", " + timeSlot + " slot is already available");
                        flag = true;
                        break;
                    } else if ((TimeSlotUtil.convertTimeToMinutes(timeSlot.getStartTime()) < TimeSlotUtil.convertTimeToMinutes(it.getEndTime()) && TimeSlotUtil.convertTimeToMinutes(timeSlot.getStartTime()) > TimeSlotUtil.convertTimeToMinutes(it.getStartTime()))
                            || (TimeSlotUtil.convertTimeToMinutes(timeSlot.getEndTime()) > TimeSlotUtil.convertTimeToMinutes(it.getStartTime()) && TimeSlotUtil.convertTimeToMinutes(timeSlot.getEndTime()) < TimeSlotUtil.convertTimeToMinutes(it.getEndTime()))) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    doctor.addAvailability(timeSlot);
                }
            }
        });
    }

    public Map<Doctor, Set<TimeSlot>> showAvailability(Specialization specialization) {
        Map<Doctor, Set<TimeSlot>> availableDoctors = new HashMap<>();
        for (Doctor doctor : doctorRepository.findBySpecialization(specialization)) {
            for (Map.Entry<TimeSlot, Boolean> entry : doctor.getAvailability().entrySet()) {
                if (entry.getValue()) {
                    availableDoctors.putIfAbsent(doctor, new HashSet<>());
                    availableDoctors.get(doctor).add(entry.getKey());
                }
            }
        }

        return availableDoctors;
    }
}

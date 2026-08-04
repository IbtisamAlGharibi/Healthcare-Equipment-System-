package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.ReservationRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.ReservationResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import com.example.HealthcareEquipmentSystem.Entities.LaboratoryStaff;
import com.example.HealthcareEquipmentSystem.Entities.Reservation;
import com.example.HealthcareEquipmentSystem.Exceptions.BadRequestException;
import com.example.HealthcareEquipmentSystem.Exceptions.ResourceNotFoundException;
import com.example.HealthcareEquipmentSystem.Repositories.EquipmentRepository;
import com.example.HealthcareEquipmentSystem.Repositories.LaboratoryStaffRepository;
import com.example.HealthcareEquipmentSystem.Repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;
    LaboratoryStaffRepository laboratoryStaffRepository;
    EquipmentRepository equipmentRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, LaboratoryStaffRepository laboratoryStaffRepository,EquipmentRepository equipmentRepository) {
        this.reservationRepository = reservationRepository;
        this.laboratoryStaffRepository = laboratoryStaffRepository;
        this.equipmentRepository = equipmentRepository;
    }



    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO, Integer staffId) {
        Equipment equipment = equipmentRepository.findByEquipmentId(reservationRequestDTO.getEquipmentId());
        LaboratoryStaff laboratoryStaff = laboratoryStaffRepository.findByLaboratoryStaffId(staffId);
        if (equipment == null) {
            throw new ResourceNotFoundException("Equipment not found with id: " + reservationRequestDTO.getEquipmentId());
        }
        if (laboratoryStaff == null) {
            throw new ResourceNotFoundException("Laboratory Staff not found with id: " + staffId);
        }

        if (Boolean.FALSE.equals(laboratoryStaff.getIsActive())) {
            throw new BadRequestException("This laboratory staff member is not active.");
        }
        if (Boolean.FALSE.equals(equipment.getIsActive())) {
            throw new BadRequestException("This equipment has been deleted and cannot be reserved.");
        }

        // 3. Simple Business Logic Check: Can only reserve if "Available"
        if (!"Available".equalsIgnoreCase(equipment.getStatus())) {
            throw new BadRequestException("Equipment cannot be reserved unless its status is Available.");
        }
        // 4. Save reservation
        Reservation newReservation = reservationRequestDTO.toEntity();
        newReservation.setLaboratoryStaff(laboratoryStaff);
        newReservation.setEquipment(equipment);
        newReservation.setStatus("Pending");
        Reservation savedReservation = reservationRepository.save(newReservation);
        return ReservationResponseDTO.fromEntity(savedReservation);
    }


    public ReservationResponseDTO approveReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findReservationById(reservationId);
        if (reservation == null) {
            throw new ResourceNotFoundException("Reservation not found with id: " + reservationId);
        }

        reservation.setStatus("Approved");
        Equipment equipment = reservation.getEquipment();
        if (equipment != null) {
            equipment.setStatus("Reserved");
            equipmentRepository.save(equipment);
        }
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.fromEntity(savedReservation);
    }


    public ReservationResponseDTO cancelReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findReservationById(reservationId);
        if (reservation == null) {
            throw new ResourceNotFoundException("Reservation not found with id: " + reservationId);
        }

        reservation.setStatus("Cancelled");
        Equipment equipment = reservation.getEquipment();
        if (equipment != null) {
            equipment.setStatus("Available");
            equipmentRepository.save(equipment);
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.fromEntity(savedReservation);
    }

    public ReservationResponseDTO getReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findReservationById(reservationId);
        if (reservation == null) {
            throw new ResourceNotFoundException("Reservation not found with id: " + reservationId);
        }
        return ReservationResponseDTO.fromEntity(reservation);
    }

    public List<ReservationResponseDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.getAllReservation();
        List<ReservationResponseDTO> response = new ArrayList<>();
        for (Reservation reservation : reservations) {
            response.add(ReservationResponseDTO.fromEntity(reservation));
        }
        return response;
    }

    public List<ReservationResponseDTO> getReservationsByStaff(Integer staffId) {
        List<Reservation> reservations = reservationRepository.findReservationsByLaboratoryStaffId(staffId);
        List<ReservationResponseDTO> response = new ArrayList<>();
        for (Reservation reservation : reservations) {
            response.add(ReservationResponseDTO.fromEntity(reservation));
        }
        return response;
    }

    public void deleteReservation(Integer id) {
        Reservation deletedReservation = reservationRepository.findReservationById(id);
        if (deletedReservation == null) {
            throw new ResourceNotFoundException("Reservation not found with id: " + id);
        }

        if ("Approved".equalsIgnoreCase(deletedReservation.getStatus())) {
            Equipment equipment = deletedReservation.getEquipment();
            if (equipment != null) {
                equipment.setStatus("Available");
                equipmentRepository.save(equipment);
            }
        }

        deletedReservation.setIsActive(false);
        reservationRepository.save(deletedReservation);
    }

}